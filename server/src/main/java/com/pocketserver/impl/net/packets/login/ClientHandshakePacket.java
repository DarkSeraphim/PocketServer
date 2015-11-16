package com.pocketserver.impl.net.packets.login;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.PacketID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x13)
public class ClientHandshakePacket extends InPacket {

    private static final int COOKIE = 0x043F57FE;
    private static final byte SECURITY = (byte) 0xCD;

    short port;
    short timestamp;
    long session, session2;

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        System.out.println("Received next packet with length, " + dg.content().readableBytes());
        if (dg.content().readInt() != COOKIE && dg.content().readByte() != SECURITY)
            return;
        port = dg.content().readShort();
        dg.content().readBytes(new byte[(int) dg.content().readByte()]);
        for (int i = 0; i < 9; i++) {
            int n = dg.content().readMedium();
            dg.content().readBytes(new byte[n]);
        }
        timestamp = dg.content().readShort();
        session = dg.content().readLong();
        session2 = dg.content().readLong();
    }

}
