package com.pocketserver.net.packet;

import java.util.Optional;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketPingUnconnectedPing extends Packet {
    private long identifier;

    @Override
    public void read(ByteBuf buf) {
        identifier = buf.readLong();
    }

    @Override
    public Optional<Packet> handle(ChannelHandlerContext ctx) {
        return Optional.of(new PacketPingUnconnectedPong(identifier).setRemote(getRemote()));
    }
}
