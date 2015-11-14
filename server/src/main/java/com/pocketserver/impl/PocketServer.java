package com.pocketserver.impl;

import java.io.PrintStream;

import com.pocketserver.impl.net.netty.PipelineInitializer;
import org.slf4j.Logger;
import org.slf4j.impl.SimpleLogger;

import com.pocketserver.Server;
import com.pocketserver.impl.console.ConsoleThread;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class PocketServer {

    public static void main(String[] args) {
        new PocketServer();
    }

    private PocketServer() {


        //TODO: Clean this up....
        System.setOut(new PrintStream(System.out, true));
        System.setErr(new PrintStream(System.err, true));

        System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");
        System.setProperty(SimpleLogger.LEVEL_IN_BRACKETS_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "false");
        System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_LOG_NAME_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "true");
        System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "[yyyy-MM-dd HH:mm:ss]");

        new ConsoleThread().start();

        Logger logger = Server.getServer().getLogger();
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
    }

}
