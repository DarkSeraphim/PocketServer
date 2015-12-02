package com.pocketserver;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.pocketserver.api.Server;
import com.pocketserver.api.command.CommandManager;
import com.pocketserver.api.event.EventBus;
import com.pocketserver.api.permissions.PermissionResolver;
import com.pocketserver.api.permissions.PocketPermissionResolver;
import com.pocketserver.api.player.Player;
import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.api.plugin.PluginManager;
import com.pocketserver.api.util.Pipeline;
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
    private final Pipeline<PermissionResolver> permissionPipeline;

    private volatile boolean running;
    private Channel channel;

    PocketServer() {
        this.directory = new File(".");
        Preconditions.checkState(directory.getAbsolutePath().indexOf('!') == -1, "PocketServer cannot be run from inside an archive");

        Server.setServer(this);
        this.eventLoopGroup = PipelineUtils.newEventLoop(6);
        this.logger = LoggerFactory.getLogger("PocketServer");
        this.pluginManager = new PluginManager(this);
        this.commandManager = new CommandManager();
        this.eventBus = new EventBus();
        getLogger().debug("Server ID: {}", Protocol.SERVER_ID);
        startListener();

        this.permissionPipeline = new Pipeline<>();

        if (new File(directory, "plugins").mkdirs()) {
            getLogger().info(PocketLogging.Server.STARTUP, "Created \"plugins\" directory");
        }

        this.running = true;
        this.pluginManager.loadPlugins();
        getPermissionPipeline().addFirst(new PocketPermissionResolver());
        getCommandManager().registerCommand(new CommandShutdown(this));
    }

    private void startListener() {
        running = true;
        // TODO: Add configuration stuff
        ChannelFutureListener listener = new ChannelFutureListener() {
            private static final int PORT = 19132;

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    channel = future.channel();
                    getLogger().info(PocketLogging.Server.STARTUP, "Listening on port {}", PORT);
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

        for (Iterator<PermissionResolver> resolvers = permissionPipeline.iterator(); resolvers.hasNext(); ) {
            PermissionResolver resolver = resolvers.next();
            try {
                resolver.close();
            } catch (Throwable cause) {
                getLogger().error(PocketLogging.Server.SHUTDOWN, "Failed to close PermissionResolver[type={}]", new Object[] {
                    resolver.getClass().getCanonicalName(),
                    cause
                });
            } finally {
                resolvers.remove();
            }
        }

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
    public File getDirectory() {
        return directory;
    }

    @Override
    public Pipeline<PermissionResolver> getPermissionPipeline() {
        return permissionPipeline;
    }
}
