package com.pocketserver.net.codec;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface EncapsulationStrategy {
    void decode(ByteBuf buf, ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception;
}
