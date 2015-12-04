package com.pocketserver;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.pocketserver.api.Server;
import com.pocketserver.api.command.ConsoleCommandExecutor;
import com.pocketserver.api.permissions.PermissionResolver;
import com.pocketserver.api.permissions.PocketPermissionResolver;
import com.pocketserver.api.player.Player;
import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.api.plugin.PluginManager;
import com.pocketserver.api.util.Pipeline;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.command.CommandShutdown;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PipelineUtils;
import com.pocketserver.net.Protocol;
import com.pocketserver.net.packet.play.PacketPlayDisconnect;
import com.pocketserver.player.PocketPlayer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PocketServer extends Server {
    private final Pipeline<PermissionResolver> permissionPipeline;
    private final Map<String, PocketPlayer> connectionMap;
    private final ConsoleCommandExecutor consoleExecutor;
    private final EventLoopGroup eventLoopGroup;
    private final ReadWriteLock connectionLock;
    private final PluginManager pluginManager;
    private final File directory;
    private final Logger logger;

    protected volatile boolean running;

    private Channel channel;

    PocketServer() throws Exception {
        this.directory = new File(".").toPath().toAbsolutePath().toFile();
        Preconditions.checkState(directory.getAbsolutePath().indexOf('!') == -1, "PocketServer cannot be run from inside an archive");

        Server.setServer(this);
        this.consoleExecutor = new ConsoleCommandExecutor(this);
        this.logger = LoggerFactory.getLogger("PocketServer");
        this.eventLoopGroup = PipelineUtils.newEventLoop(6);
        this.connectionLock = new ReentrantReadWriteLock();
        this.pluginManager = new PluginManager(this);
        this.permissionPipeline = Pipeline.of();
        this.connectionMap = Maps.newHashMap();

        getLogger().debug("Directory: {}", directory.toString());
        getLogger().debug("Server ID: {}", Protocol.SERVER_ID);

        if (new File(directory, "plugins").mkdirs()) {
            getLogger().info(PocketLogging.Server.STARTUP, "Created \"plugins\" directory");
        }

        startListener().await();

        getPermissionPipeline().addFirst(new PocketPermissionResolver());
        getPluginManager().registerCommand(null, new CommandShutdown(this));
        this.pluginManager.loadPlugins();
    }

    private CountDownLatch startListener() {
        CountDownLatch latch = new CountDownLatch(1);

        // TODO: Add configuration stuff
        ChannelFutureListener listener = new ChannelFutureListener() {
            private static final int PORT = 19132;

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    getLogger().info(PocketLogging.Server.STARTUP, "Listening on port {}", PORT);
                    channel = future.channel();
                    latch.countDown();
                    running = true;
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

        return latch;
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

        Packet packet = new PacketPlayDisconnect("Server is shutting down!");
        broadcast(packet);

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
            // NOP
        }

        getLogger().info("Thanks for using PocketServer!");
        System.exit(0);
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
    public File getDirectory() {
        return directory;
    }

    @Override
    public Pipeline<PermissionResolver> getPermissionPipeline() {
        return permissionPipeline;
    }

    @Override
    public Optional<Player> getPlayer(String username) {
        connectionLock.readLock().lock();
        try {
            return Optional.ofNullable(connectionMap.get(username.toLowerCase()));
        } finally {
            connectionLock.readLock().unlock();
        }
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
        connectionLock.readLock().lock();
        try {
            return ImmutableList.copyOf(connectionMap.values());
        } finally {
            connectionLock.readLock().unlock();
        }
    }

    @Override
    public ConsoleCommandExecutor getConsole() {
        return consoleExecutor;
    }

    public void addPlayer(PocketPlayer player) {
        Preconditions.checkNotNull(player, "player should not be null!");
        connectionLock.writeLock().lock();
        try {
            connectionMap.put(player.getName(), player);
        } finally {
            connectionLock.writeLock().unlock();
        }
    }

    public void removePlayer(PocketPlayer player) {
        Preconditions.checkNotNull(player, "player should not be null!");
        connectionLock.writeLock().lock();
        try {
            for (Iterator<PocketPlayer> players = connectionMap.values().iterator(); players.hasNext(); ) {
                if (players.next() == player) {
                    players.remove();
                    break;
                }
            }
        } finally {
            connectionLock.writeLock().unlock();
        }
    }

    public void broadcast(Packet packet) {
        broadcast(packet, player -> true);
    }

    public void broadcast(Packet packet, Predicate<PocketPlayer> predicate) {
        connectionLock.writeLock().lock();
        try {
            connectionMap.values().stream().filter(predicate::apply).forEach(player -> player.unsafe().send(packet));
        } finally {
            connectionLock.writeLock().unlock();
        }
    }

    public Optional<PocketPlayer> getPlayer(InetSocketAddress address) {
        connectionLock.readLock().lock();
        try {
            return connectionMap.values().stream().filter(player -> {
                InetSocketAddress playerAddress = player.getAddress();
                if (playerAddress.getPort() == address.getPort()) {
                    return Arrays.equals(playerAddress.getAddress().getAddress(), address.getAddress().getAddress());
                }
                return false;
            }).findFirst();
        } finally {
            connectionLock.readLock().unlock();
        }
    }
}
