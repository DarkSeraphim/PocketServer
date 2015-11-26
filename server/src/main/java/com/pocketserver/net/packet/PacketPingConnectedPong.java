package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPingConnectedPong extends Packet {
    private final long identifier;

    public PacketPingConnectedPong(long identifier) {
        this.identifier = identifier;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeLong(identifier);
    }
}
