package com.pocketserver.impl.net.packets.login;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x09)
public class ClientConnectPacket extends Packet {
    private long clientId;
    private long session;

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
       // System.out.println("0x09 is a decode packet.");
        ByteBuf content = dg.content();

        clientId = content.readLong();
        session = content.readLong();
        boolean b = content.readBoolean();

        new ServerHandshakePacket(session,dg.sender()).sendPacket(ctx, dg.sender());
    }

    public long getClientId() {
        return clientId;
    }

    public long getSession() {
        return session;
    }
}
