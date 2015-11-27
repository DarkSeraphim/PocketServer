package com.pocketserver.net.codec;

import java.util.List;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface EncapsulationStrategy {
    void decode(ByteBuf buf, ChannelHandlerContext ctx, List<Packet> out) throws Exception;
}
