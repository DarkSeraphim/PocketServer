package com.pocketserver.net.packet.connect;

import com.pocketserver.net.packet.AbstractEncapsulatedPacket;
import io.netty.buffer.ByteBuf;

public class PacketConnectOpenResponse extends AbstractEncapsulatedPacket {
    private final long serverTimestamp;
    private final long clientTimestamp;

    public PacketConnectOpenResponse(long clientTimestamp) {
        this.serverTimestamp = System.currentTimeMillis();
        this.clientTimestamp = clientTimestamp;
    }

    // TODO: https://github.com/NiclasOlofsson/MiNET/blob/master/src/MiNET/MiNET/Net/MCPE%20Protocol%20Documentation.md#package-connection-request-accepted-0x10
    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(4);
        {
            buf.writeByte(~127);
            buf.writeByte(~0);
            buf.writeByte(~0);
            buf.writeByte(~1);
        }
        buf.writeShort(19132);
        buf.writeLong(clientTimestamp);
        buf.writeLong(serverTimestamp);
    }
}
