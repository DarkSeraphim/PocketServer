package com.pocketserver.net.packet.ping;

import java.util.List;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketPingConnectedPing extends Packet {
    private long timestamp;

    public PacketPingConnectedPing() {
        super(PacketRegistry.PacketType.CONNECTED_PING);
    }

    @Override
    public void read(ByteBuf buf) throws Exception {
        timestamp = buf.readLong();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {
        out.add(new PacketPingConnectedPong(timestamp));
    }
}
