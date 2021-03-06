package com.pocketserver.net.codec;

import java.util.List;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface EncapsulationStrategy {

    void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out, short length) throws Exception;

    ByteBuf encode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception;
}
