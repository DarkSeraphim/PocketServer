package com.pocketserver.net.packet.connection.ping;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPingConnectedPong extends Packet {
    private final long timestamp;

    public PacketPingConnectedPong(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeLong(timestamp);
    }
}
