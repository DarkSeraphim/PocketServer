package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketPingUnconnectedPong extends Packet {
    private final long pingTimestamp;

    public PacketPingUnconnectedPong(long pingTimestamp) {
        this.pingTimestamp = pingTimestamp;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(pingTimestamp);
        buf.writeLong(Protocol.SERVER_ID);
        buf.writeBytes(Protocol.OFFLINE_MESSAGE_ID);
        writeString(buf, Protocol.TEMP_IDENTIFIER);
    }
}
