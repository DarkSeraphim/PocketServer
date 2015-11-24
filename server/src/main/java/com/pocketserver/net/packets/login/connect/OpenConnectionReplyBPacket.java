package com.pocketserver.net.packets.login.connect;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;

import com.pocketserver.net.Protocol;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x08)
public class OpenConnectionReplyBPacket extends Packet {
    private final int clientPort;
    private final int mtu;

    protected OpenConnectionReplyBPacket(int mtu, int clientPort) {
        this.mtu = mtu;
        this.clientPort = clientPort;
    }

    @Override
    public DatagramPacket encode(DatagramPacket dg) {
        dg.content().writeByte(this.getPacketID());
        writeMagic(dg.content());
        dg.content().writeLong(Protocol.TEMP_SERVER_ID);
        dg.content().writeShort(clientPort);
        dg.content().writeShort(mtu);
        dg.content().writeByte(0);
        return dg;
    }
}
