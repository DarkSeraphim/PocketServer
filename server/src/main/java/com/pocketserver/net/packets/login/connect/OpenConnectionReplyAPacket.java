package com.pocketserver.net.packets.login.connect;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;

import com.pocketserver.net.Protocol;
import com.pocketserver.net.util.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x06)
public class OpenConnectionReplyAPacket extends Packet {
    private final int mtu;

    protected OpenConnectionReplyAPacket(int mtu) {
        this.mtu = mtu;
    }

    @Override
    public void encode(ByteBuf content) {
        content.writeByte(0x06);
        PacketUtils.writeMagic(content);
        content.writeLong(Protocol.TEMP_SERVER_ID);
        content.writeByte(0);
        content.writeShort(mtu);
    }
}
