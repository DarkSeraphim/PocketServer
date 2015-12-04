package com.pocketserver.net.codec;

import java.util.List;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface EncapsulationStrategy {
    void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception;

    ByteBuf encode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception;

    default int peekLength(ByteBuf buf) {
        return buf.readShort() / 8;
    }
}
