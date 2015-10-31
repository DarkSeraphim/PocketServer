package com.pocketserver;

import com.pocketserver.event.EventBus;
import com.pocketserver.net.netty.PocketServerHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class PocketServer {
	
	private static PocketServer server;
	
	public static PocketServer getServer() {
		return server;
	}

    public static void main(String[] args) {
        server = new PocketServer();
    }
    
    private EventBus eventBus;
    private boolean running = true;

    private PocketServer() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            {
                bootstrap.group(group);
                bootstrap.handler(new PocketServerHandler());
                bootstrap.channel(NioDatagramChannel.class);
                bootstrap.option(ChannelOption.SO_BROADCAST, true);
            }
            bootstrap.bind(19132).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            running = false;
        }
    }
	
	public boolean isRunning() {
		return running;
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}

}
