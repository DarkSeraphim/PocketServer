package com.pocketserver.net;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pocketserver.net.netty.PacketDecoder;
import com.pocketserver.net.netty.PacketEncoder;
import com.pocketserver.net.netty.PocketServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * @author Connor Spencer Harries
 */
public final class PipelineUtils {
    private static final boolean useEpoll;

    static {
        Logger logger = LoggerFactory.getLogger("PocketServer");
        boolean tempUseEpoll = false;
        if (!PlatformDependent.isWindows() && Boolean.getBoolean("pocket.use-epoll")) {
            if (Epoll.isAvailable()) {
                tempUseEpoll = true;
                logger.info("Epoll is supported by your system!");
            } else {
                logger.info("Epoll is not supported by your system!");
            }
        }
        useEpoll = tempUseEpoll;
    }

    public static final ChannelInitializer<?> INITIALIZER = new ChannelInitializer<Channel>() {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("encoder", new PacketEncoder());
            pipeline.addLast("decoder", new PacketDecoder());
            pipeline.addLast("handler", new PocketServerHandler());
        }
    };

    public static final Marker NETWORK_MARKER = MarkerFactory.getMarker("NETWORK");

    public static Class<? extends Channel> getChannelClass() {
        if (useEpoll) {
            return EpollDatagramChannel.class;
        } else {
            return NioDatagramChannel.class;
        }
    }

    public static EventLoopGroup newEventLoop(int numThreads) {
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder().setNameFormat("Netty IO Thread #%1$d");
        ExecutorService executor = Executors.newFixedThreadPool(numThreads, builder.build());
        if (useEpoll) {
            return new EpollEventLoopGroup(0, executor);
        } else {
            return new NioEventLoopGroup(0, executor);
        }
    }

    private PipelineUtils() {
      throw new UnsupportedOperationException("PipelineUtils cannot be instantiated!");
    }
}
