package com.pocketserver.net.netty;

import com.pocketserver.net.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PocketServerHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
        packet.handle(ctx).ifPresent(ctx::writeAndFlush);
    }
}
