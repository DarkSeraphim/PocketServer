package com.pocketserver.impl;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.pocketserver.command.PermissionResolver;
import com.pocketserver.impl.player.PocketPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.SimpleLogger;

import com.pocketserver.Server;
import com.pocketserver.command.CommandManager;
import com.pocketserver.event.EventBus;
import com.pocketserver.impl.console.ConsoleThread;
import com.pocketserver.impl.gui.ConsoleWindow;
import com.pocketserver.impl.net.netty.PipelineInitializer;
import com.pocketserver.impl.player.PlayerRegistry;
import com.pocketserver.player.Player;
import com.pocketserver.plugin.PluginManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class PocketServer extends Server {

    private final Logger logger;
    private final EventBus eventBus;
    private final ConsoleWindow console;
    private final PluginManager pluginManager;
    private final ExecutorService executorService; //TODO: Implement the same instance of an executor service.

    private PermissionResolver permissionResolver;
    
    PocketServer() {
        Server.setServer(this);
        this.console = new ConsoleWindow(getOnlinePlayers());
        this.logger = LoggerFactory.getLogger("PocketServer");
        this.pluginManager = new PluginManager(this);
        this.executorService = new ScheduledThreadPoolExecutor(10); //TODO: Configure this
        this.eventBus = new EventBus(executorService);
        /*
         * Not converted to a lambda as the PermissionResolver interface may be subject to
         * drastic changes with each update.
         */
        this.permissionResolver = new PermissionResolver() {
            @Override
            public boolean checkPermission(Player player, String permission) {
                if (player != null && player instanceof PocketPlayer) {
                    PocketPlayer actualPlayer = (PocketPlayer) player;
                    Boolean value = actualPlayer.getPermissions().get(permission);
                    return value != null && value;
                }
                return false;
            }
        };

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

    //Start api design
    
    public ConsoleWindow getConsoleWindow() {
        return console;
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
}
