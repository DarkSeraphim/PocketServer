package com.pocketserver.impl.net.packets.login.connect;

import com.pocketserver.impl.net.OutPacket;
import com.pocketserver.impl.net.PacketID;
import com.pocketserver.impl.net.Protocol;
import com.pocketserver.impl.net.util.PacketUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

@PacketID({ 0x1C, 0x1D })
public class UnconnectedPongPacket extends OutPacket {

    private final int packetId;
    private final long id;

    protected UnconnectedPongPacket(long id) {
        this.packetId = getPacketID();
        this.id = id;
    }

    protected UnconnectedPongPacket(int packetId, long id) {
        this.packetId = (packetId == 0x1C || packetId == 0x1D) ? packetId : 0x1C;
        this.id = id;
    }

    @Override
    public DatagramPacket encode(DatagramPacket buf) {
        ByteBuf content = buf.content();
        content.writeByte(packetId);
        content.writeLong(id);
        content.writeLong(Protocol.TEMP_SERVER_ID);
        writeMagic(content);
        PacketUtils.writeString(content, Protocol.TEMP_IDENTIFIER);
        return buf;
    }

}