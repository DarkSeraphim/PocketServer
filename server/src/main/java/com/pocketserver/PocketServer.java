package com.pocketserver;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.pocketserver.api.command.PermissionResolver;
import com.pocketserver.player.PocketPlayer;
import com.sun.corba.se.impl.javax.rmi.PortableRemoteObject;
import com.sun.corba.se.impl.naming.pcosnaming.PersistentBindingIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class PocketServer extends Server {

    private final Logger logger;
    private final File directory;
    private final EventBus eventBus;
    private final PluginManager pluginManager;
    private final ExecutorService executorService; //TODO: Implement the same instance of an executor service.

    private PermissionResolver permissionResolver;

    PocketServer() {
        this.directory = new File(".");
        Preconditions.checkState(directory.getAbsolutePath().indexOf('!') == -1, "PocketServer cannot be run from inside an archive");

        Server.setServer(this);
        this.logger = LoggerFactory.getLogger("PocketServer");
        this.pluginManager = new PluginManager(this);
        this.executorService = new ScheduledThreadPoolExecutor(10); //TODO: Configure this
        this.eventBus = new EventBus(executorService);
        this.permissionResolver = new PermissionResolver() {
            // TODO: Make less ugly
            private LoadingCache<String, List<String>> permissions = CacheBuilder.newBuilder().build(new CacheLoader<String, List<String>>() {
                @Override
                public List<String> load(String key) {
                    return Lists.newArrayList();
                }
            });

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
                return permissions.getUnchecked(Preconditions.checkNotNull(player, "player should not be null!").getName());
            }
        };

        if (new File(directory, "plugins").mkdirs()) {
            getLogger().info("Created \"plugins\" directory");
        }

        setProperties();
        startThreads();
    }

    private void startThreads() {
        new ConsoleThread(this).start();
        startNetty();
    }

    private void startNetty() {
        this.executorService.submit(() -> {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap boot = new Bootstrap();
                {
                    boot.group(group);
                    boot.handler(new PipelineInitializer());
                    boot.channel(NioDatagramChannel.class);
                    boot.option(ChannelOption.SO_BROADCAST, true);
                }
                ChannelFuture future = boot.bind(19132).sync();
                logger.info("Successfully bound to *:19132");
                logger.info("Server is done loading!");
                future.channel().closeFuture().sync();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                logger.info("Goodbye.");
                group.shutdownGracefully();
            }
        });
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
        return true;
    }

    @Override
    public List<? extends Player> getOnlinePlayers() {
        return PlayerRegistry.get().getPlayers();
    }

    @Override
    public CommandManager getCommandManager() {
        return new CommandManager();
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
