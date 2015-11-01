package com.pocketserver.impl.net.packets.ping;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.PacketID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x00)
public class PingPacket extends InPacket {

	long identifier;
	
    @Override
    public void decode(ChannelHandlerContext ctx, DatagramPacket dg) {
    	identifier = dg.content().readLong();
        new PongPacket(identifier).sendGame(0x84, ctx, dg.sender());
    }
}