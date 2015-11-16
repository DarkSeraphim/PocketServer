package com.pocketserver.impl.net.packets.login;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.PacketID;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x09)
public class ClientConnectPacket extends InPacket {

    private long clientId;
    private long session;

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
       // System.out.println("0x09 is a decode packet.");
        ByteBuf content = dg.content();
        System.out.println("Readable: " + content.readableBytes());
        clientId = content.readLong();
        session = content.readLong();
        boolean b = content.readBoolean();

        new ServerHandshakePacket(session,dg.sender()).sendPacket(ctx, dg.sender());
        System.out.println("should be 0: " + content.readableBytes());
        //System.out.println("Received and sending new packet.");
        //.sendGame(0x84, EncapsulationStrategy.COUNT, 0x02F001, 0, ctx, dg.sender());
    }

}
