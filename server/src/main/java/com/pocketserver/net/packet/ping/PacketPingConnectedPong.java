package com.pocketserver.net.packet.ping;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;

public class PacketPingConnectedPong extends Packet {
    private final long timestamp;

    public PacketPingConnectedPong(long timestamp) {
        super(PacketRegistry.PacketType.CONNECTED_PONG);
        this.timestamp = timestamp;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeLong(timestamp);
    }
}
