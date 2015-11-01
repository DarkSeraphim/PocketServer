package com.pocketserver.net.packets.login;

import com.pocketserver.net.InPacket;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x01)
public class UnconnectedPingPacket extends InPacket {

    @Override
    public void decode(ChannelHandlerContext ctx, DatagramPacket dg) {
        ByteBuf content = dg.content();
        UnconnectedPongPacket packet = new UnconnectedPongPacket(0x1C, content.readLong());
        if (content.readLong() == Packet.MAGIC_1 && content.readLong() == Packet.MAGIC_2)
        	packet.sendLogin(ctx, dg.sender());
    }
    
}