package com.pocketserver.impl.net.packets.login.connect;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;

import com.pocketserver.impl.net.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x01)
public class UnconnectedPingPacket extends Packet {
    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        ByteBuf content = dg.content();
        UnconnectedPongPacket packet = new UnconnectedPongPacket(0x1C, content.readLong());
        System.out.println(content.readableBytes());
        if (content.readLong() == Protocol.MAGIC_1 && content.readLong() == Protocol.MAGIC_2)
            packet.sendPacket(ctx, dg.sender());
        else {System.out.println("weird...");}
    }
}