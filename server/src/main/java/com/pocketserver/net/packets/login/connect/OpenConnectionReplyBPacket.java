package com.pocketserver.net.packets.login.connect;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;

import com.pocketserver.net.Protocol;
import com.pocketserver.net.util.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x08)
public class OpenConnectionReplyBPacket extends Packet {
    private final int mtu;

    protected OpenConnectionReplyBPacket(int mtu) {
        this.mtu = mtu;
    }

    @Override
    public void encode(ByteBuf content) {
        content.writeByte(this.getPacketID());
        PacketUtils.writeMagic(content);
        content.writeLong(Protocol.TEMP_SERVER_ID);
        content.writeShort(getRemote().getPort());
        content.writeShort(mtu);
        content.writeByte(0);
    }
}
