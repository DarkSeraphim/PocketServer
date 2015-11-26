package com.pocketserver;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.pocketserver.api.Server;
import com.pocketserver.api.command.CommandManager;
import com.pocketserver.api.event.EventBus;
import com.pocketserver.api.permissions.PermissionResolver;
import com.pocketserver.api.player.Player;
import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.api.plugin.PluginManager;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.command.CommandShutdown;
import com.pocketserver.net.PipelineUtils;
import com.pocketserver.net.Protocol;
import com.pocketserver.player.PlayerRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PocketServer extends Server {
    private final Logger logger;
    private final File directory;
    private final EventBus eventBus;
    private final PluginManager pluginManager;
    private final CommandManager commandManager;
    private final EventLoopGroup eventLoopGroup;

    // TODO: Possibly replace with Pipeline<PermissionResolver>
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
        this.eventBus = new EventBus();

        // TODO: Replace with Pipeline<PermissionResolver>
        this.permissionResolver = new PermissionResolver() {
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
            getLogger().info(PocketLogging.Server.STARTUP, "Created \"plugins\" directory");
        }

        this.eventLoopGroup = PipelineUtils.newEventLoop(6);
        this.running = true;

        getCommandManager().registerCommand(new CommandShutdown(this));

        startListener();
    }

    private void startListener() {
        // TODO: Add configuration stuff
        ChannelFutureListener listener = new ChannelFutureListener() {
            private static final int PORT = 19132;

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    channel = future.channel();
                    getLogger().info(PocketLogging.Server.STARTUP, "Listening on port {}", PORT);
                    getLogger().debug("Server ID: {}", Protocol.SERVER_ID);
                } else {
                    getLogger().error(PocketLogging.Server.STARTUP, "Could not bind to {}", PORT, future.cause());
                    shutdown();
                }
            }
        };

        new Bootstrap()
                .group(eventLoopGroup)
                .handler(PipelineUtils.INITIALIZER)
                .channel(PipelineUtils.getChannelClass())
                .option(ChannelOption.SO_BROADCAST, true)
                .bind(19132)
                .addListener(listener);
    }

    @Override
    public void shutdown() {
        running = false;

        if (channel != null && channel.isOpen()) {
            channel.close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        getLogger().info(PocketLogging.Server.SHUTDOWN, "Closing listener {}", channel);
                    } else {
                        getLogger().error(PocketLogging.Server.SHUTDOWN, "Failed to close listener", future.cause());
                    }
                }
            }).syncUninterruptibly();
        }

        // TODO: Kick connected clients

        permissionResolver.close();

        getLogger().info(PocketLogging.Server.SHUTDOWN, "Disabling plugins");
        Lists.reverse(getPluginManager().getPlugins()).stream().filter(Plugin::isEnabled).forEachOrdered(plugin -> {
            getPluginManager().setEnabled(plugin, false);
        });

        getLogger().info(PocketLogging.Server.SHUTDOWN, "Closing IO threads");
        eventLoopGroup.shutdownGracefully();
        try {
            // Don't bother if it already shut down
            if (!eventLoopGroup.isShutdown() && eventLoopGroup.isShuttingDown()) {
                eventLoopGroup.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            }
        } catch (Exception e) {

        }

        getLogger().info("Thanks for using PocketServer!");
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
