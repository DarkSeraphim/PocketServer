package com.pocketserver.impl.net.packets.login.connect;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;

import com.pocketserver.impl.net.Protocol;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x06)
public class OpenConnectionReplyAPacket extends Packet {
    private final int mtu;

    protected OpenConnectionReplyAPacket(int mtu) {
        this.mtu = mtu;
    }

    @Override
    public DatagramPacket encode(DatagramPacket dg) {
        dg.content().writeByte(0x06);
        writeMagic(dg.content());
        dg.content().writeLong(Protocol.TEMP_SERVER_ID);
        dg.content().writeByte(0);
        dg.content().writeShort(mtu);
        return dg;
    }
}
