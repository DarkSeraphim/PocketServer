package com.pocketserver.net.packet;

import java.util.Optional;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketConnectionRequest extends Packet {
    private long timestamp;
    private long clientId;
    private byte sec;

    @Override
    public void read(ByteBuf buf) throws Exception {
        clientId = buf.readLong();
        timestamp = buf.readLong();
        sec = buf.readByte();
    }

    @Override
    public Optional<Packet> handle(ChannelHandlerContext ctx) throws Exception {
        return Optional.of(new PacketConnectionRequestAccepted(timestamp));
    }
}
