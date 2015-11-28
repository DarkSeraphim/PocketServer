package com.pocketserver.net.packet.connection;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayDisconnect extends Packet {
    private final String reason;

    public PacketPlayDisconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(buf, reason);
    }
}
