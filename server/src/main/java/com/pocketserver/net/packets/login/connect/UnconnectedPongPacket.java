package com.pocketserver.net.packets.login.connect;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.Protocol;
import com.pocketserver.net.util.PacketUtils;
import io.netty.buffer.ByteBuf;

@PacketID({ 0x1C, 0x1D })
public class UnconnectedPongPacket extends Packet {
    private final int packetId;
    private final long id;

    protected UnconnectedPongPacket(int packetId, long id) {
        this.packetId = (packetId == 0x1C || packetId == 0x1D) ? packetId : 0x1C;
        this.id = id;
    }

    @Override
    public void encode(ByteBuf content) {
        content.writeByte(packetId);
        content.writeLong(id);
        content.writeLong(Protocol.TEMP_SERVER_ID);
        PacketUtils.writeMagic(content);
        PacketUtils.writeString(content, Protocol.TEMP_IDENTIFIER);
    }
}