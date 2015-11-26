package com.pocketserver.net.netty;

import com.pocketserver.net.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

//TODO: Properly use decoders (thx connur)
public class PacketEncoder extends MessageToMessageEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> list) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        packet.encode(buffer);
        list.add(new DatagramPacket(buffer, packet.getRemote()));
        System.out.println("Sweg");
    }
}
