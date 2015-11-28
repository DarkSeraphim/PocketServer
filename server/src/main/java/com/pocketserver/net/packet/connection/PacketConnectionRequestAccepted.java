package com.pocketserver.net.packet.connection;

import com.pocketserver.net.packet.AbstractEncapsulatedPacket;
import io.netty.buffer.ByteBuf;

public class PacketConnectionRequestAccepted extends AbstractEncapsulatedPacket {
    private final long serverTimestamp;
    private final long clientTimestamp;

    public PacketConnectionRequestAccepted(long clientTimestamp) {
        this.serverTimestamp = System.currentTimeMillis();
        this.clientTimestamp = clientTimestamp;
    }

    // TODO: https://github.com/NiclasOlofsson/MiNET/blob/master/src/MiNET/MiNET/Net/MCPE%20Protocol%20Documentation.md#package-connection-request-accepted-0x10
    @Override
    public void write(ByteBuf buf) {
        for (int i = 0; i < 2; i++) {
            buf.writeByte(0x04);
            for (int x = 0; x < 4; x++) {
                buf.writeByte(0xFF);
            }
            buf.writeShort(19132);
        }
        buf.writeLong(clientTimestamp);
        buf.writeLong(serverTimestamp);
    }
}
