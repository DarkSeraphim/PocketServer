package com.pocketserver;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.pocketserver.api.command.PermissionResolver;
import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.command.CommandShutdown;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.impl.SimpleLogger;

import com.pocketserver.api.Server;
import com.pocketserver.api.command.CommandManager;
import com.pocketserver.api.event.EventBus;
import com.pocketserver.console.ConsoleThread;
import com.pocketserver.net.netty.PipelineInitializer;
import com.pocketserver.player.PlayerRegistry;
import com.pocketserver.api.player.Player;
import com.pocketserver.api.plugin.PluginManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class PocketServer extends Server {
    private static final Marker LISTENER_SHUTDOWN = MarkerFactory.getMarker("LISTENER_SHUTDOWN");
    private static final Marker LISTENER_INIT = MarkerFactory.getMarker("LISTENER_INIT");

    private final Logger logger;
    private final File directory;
    private final EventBus eventBus;
    private final PluginManager pluginManager;
    private final CommandManager commandManager;
    private final EventLoopGroup eventLoopGroup;

    // TODO: Implement elsewhere, probably in a scheduler class
    private final ExecutorService executorService;

    private PermissionResolver permissionResolver;
    private volatile boolean running;
    private Channel channel;

    PocketServer() {
        this.directory = new File(".");
        Preconditions.checkState(directory.getAbsolutePath().indexOf('!') == -1, "PocketServer cannot be run from inside an archive");

        Server.setServer(this);
        this.commandManager = new CommandManager();
        this.pluginManager = new PluginManager(this);
        this.logger = LoggerFactory.getLogger("PocketServer");
        this.executorService = new ScheduledThreadPoolExecutor(10); //TODO: Configure this
        this.eventBus = new EventBus(executorService);
        this.permissionResolver = new PermissionResolver() {
            @Override
            public void close() throws IOException {
                // NOP
            }

            // TODO: Make less ugly
            private Cache<String, List<String>> cache = CacheBuilder.newBuilder().maximumSize(250).build();

            @Override
            public void setPermission(Player player, String permission, boolean state) {
                Preconditions.checkNotNull(permission, "permission should not be null!");
                Preconditions.checkArgument(permission.length() > 0, "permission should not be empty!");

                permission = permission.toLowerCase();
                List<String> target = getPermissions(player);
                if (state) {
                    target.add(permission);
                } else {
                    target.remove(permission);
                }
            }

            @Override
            public boolean checkPermission(Player player, String permission) {
                return getPermissions(player).contains(permission.toLowerCase());
            }

            @Override
            public List<String> getPermissions(Player player) {
                List<String> permissions = cache.getIfPresent(Preconditions.checkNotNull(player, "player should not be null!").getName());
                if (permissions == null) {
                    permissions = Lists.newArrayList();
                    cache.put(player.getName(), permissions);
                }
                return permissions;
            }
        };

        if (new File(directory, "plugins").mkdirs()) {
            getLogger().info("Created \"plugins\" directory");
        }

        // TODO: Add epoll support
        this.eventLoopGroup = new NioEventLoopGroup();
        this.running = true;

        getCommandManager().registerCommand(new CommandShutdown(this));

        setProperties();
        startThreads();
    }

    private void startThreads() {
        new ConsoleThread(this).start();
        startListener();
    }

    private void startListener() {
        // TODO: Add configuration stuff
        ChannelFutureListener listener = new ChannelFutureListener() {
            static final int PORT = 19132;

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    channel = future.channel();
                    getLogger().info(LISTENER_INIT, "Listening on port {}", PORT);
                } else {
                    getLogger().error(LISTENER_INIT, "Could not bind to {}", PORT, future.cause());
                    shutdown();
                }
            }
        };

        new Bootstrap()
            .group(eventLoopGroup)
            .handler(new PipelineInitializer())
            .channel(NioDatagramChannel.class)
            .option(ChannelOption.SO_BROADCAST, true)
            .bind(19132)
            .addListener(listener);
    }
    
    private void setProperties() {
        System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");
        System.setProperty(SimpleLogger.LEVEL_IN_BRACKETS_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "false");
        System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_LOG_NAME_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "true");
        System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "[yyyy-MM-dd HH:mm:ss]");
    }

    @Override
    public void shutdown() {
        running = false;

        if (channel != null) {
            channel.close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        getLogger().info(LISTENER_SHUTDOWN, "Closing listener {}", channel);
                    } else {
                        getLogger().error(LISTENER_SHUTDOWN, "Failed to close listener", future.cause());
                    }
                }
            }).syncUninterruptibly();
        }

        // TODO: Kick connected clients

        try {
            // Allows people writing resolvers to close their connection pools and such.
            permissionResolver.close();
        } catch (IOException ex) {
            getLogger().error(LISTENER_SHUTDOWN, "Failed to close permission resolver", ex);
        }

        getLogger().info(LISTENER_SHUTDOWN, "Disabling plugins");
        Lists.reverse(getPluginManager().getPlugins()).stream().filter(Plugin::isEnabled).forEachOrdered(plugin -> {
            getPluginManager().setEnabled(plugin, false);
            try {
                ClassLoader loader = plugin.getClass().getClassLoader();
                if (loader instanceof URLClassLoader) {
                    URLClassLoader classLoader = (URLClassLoader) loader;
                    classLoader.close();

                    if (PlatformDependent.isWindows()) {
                        System.gc();
                    }
                }
            } catch (Exception ex) {
                // NOP
            }
        });

        getLogger().info(LISTENER_SHUTDOWN, "Closing IO threads");
        eventLoopGroup.shutdownGracefully();
        try {
            eventLoopGroup.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {

        }
        getLogger().info(LISTENER_SHUTDOWN, "Thanks for using PocketServer!");
        System.exit(0);
    }

    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public List<? extends Player> getOnlinePlayers() {
        return PlayerRegistry.get().getPlayers();
    }

    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public void setPermissionResolver(PermissionResolver permissionResolver) {
        this.permissionResolver = Preconditions.checkNotNull(permissionResolver, "permissionResolver should not be null!");
    }

    @Override
    public PermissionResolver getPermissionResolver() {
        return permissionResolver;
    }

    @Override
    public File getDirectory() {
        return directory;
    }
}
