package com.pocketserver.net.packet.connection.ping;

import java.util.List;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketPingUnconnectedPing extends Packet {
    private long timestamp;

    @Override
    public void read(ByteBuf buf) {
        timestamp = buf.readLong();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {
        out.add(new PacketPingUnconnectedPong(timestamp));
    }
}
