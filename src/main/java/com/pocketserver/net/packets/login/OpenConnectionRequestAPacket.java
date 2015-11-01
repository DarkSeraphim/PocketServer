package com.pocketserver.net.packets.login;

import com.pocketserver.net.InPacket;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x05)
public class OpenConnectionRequestAPacket extends InPacket {

	byte proto;
	int mtu;
	
	@Override
	public void decode(ChannelHandlerContext ctx, DatagramPacket dg) {
		ByteBuf buf = dg.content();
		long magic1 = buf.readLong();
		long magic2 = buf.readLong();
        if (magic1 == Packet.MAGIC_1 && magic2 == Packet.MAGIC_2) {
        	proto = buf.readByte();
        	mtu = buf.readableBytes();
        	System.out.println(proto + " : " + mtu);
        	if (proto == Protocol.RAKNET) {
        		ctx.write(new OpenConnectionReplyAPacket(mtu).encode(new DatagramPacket(Unpooled.buffer(), dg.sender())));
        	} else {
        		ctx.write(new IncompatibleProtocolPacket().encode(new DatagramPacket(Unpooled.buffer(), dg.sender())));
        	}
        	ctx.flush();
        }
	}

}