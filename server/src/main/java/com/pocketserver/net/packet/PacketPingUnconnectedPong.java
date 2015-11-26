package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketPingUnconnectedPong extends Packet {
    private final long identifier;

    public PacketPingUnconnectedPong(long identifier) {
        this.identifier = identifier;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(identifier);
        buf.writeLong(Protocol.SERVER_ID);
        writeMagic(buf);
        writeString(buf, Protocol.TEMP_IDENTIFIER);
    }
}
