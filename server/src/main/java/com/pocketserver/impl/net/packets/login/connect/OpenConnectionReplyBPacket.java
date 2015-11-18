package com.pocketserver.impl.net.packets.login.connect;

import com.pocketserver.impl.net.OutPacket;
import com.pocketserver.impl.net.PacketID;

import com.pocketserver.impl.net.Protocol;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x08)
public class OpenConnectionReplyBPacket extends OutPacket {

    private final int mtu, clientPort;

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
