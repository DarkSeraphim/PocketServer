package com.pocketserver.net.packet;

import java.util.Optional;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketPingConnectedPing extends Packet {
    private long identifier;

    @Override
    public void read(ByteBuf buf) throws Exception {
        identifier = buf.readLong();
    }

    @Override
    public Optional<Packet> handle(ChannelHandlerContext ctx) throws Exception {
        return Optional.of(new PacketPingConnectedPong(identifier).setRemote(getRemote()));
    }
}
