package com.pocketserver.impl.net.packets.udp;

import com.pocketserver.impl.net.OutPacket;
import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.util.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

public abstract class EncapsulatedPacket extends OutPacket {
    private static int ctr = -1;

    @Override
    public Packet sendPacket(ChannelHandlerContext ctx, InetSocketAddress address) {
        DatagramPacket packet = new DatagramPacket(Unpooled.buffer(), address);
        ByteBuf content = packet.content();
        content.writeByte(0x80);
        content.writeBytes(PacketUtils.getTriad(ctr++));
        content.writeByte(0x00);

        DatagramPacket ePacket = encode(new DatagramPacket(Unpooled.buffer(), address));
        ByteBuf filledContent = encode(ePacket).content();
        int readableBytes = filledContent.readableBytes();
        content.writeShort(readableBytes*8);
        content.writeBytes(filledContent.readBytes(readableBytes).array());
        ctx.writeAndFlush(packet);
        return this;
    }
}
