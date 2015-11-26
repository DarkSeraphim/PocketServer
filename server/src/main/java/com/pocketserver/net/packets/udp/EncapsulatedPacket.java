package com.pocketserver.net.packets.udp;

import com.pocketserver.net.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

public abstract class EncapsulatedPacket extends Packet {
    private static int ctr = -1;

    @Override
    public void encode(ByteBuf content) {
        content.writeByte(0x80);
        content.writeMedium(ctr++);
        content.writeByte(0x00);
    }

    /*
    @Override
    public Packet sendPacket(ChannelHandlerContext ctx, InetSocketAddress address) {
        DatagramPacket packet = new DatagramPacket(Unpooled.buffer(), address);
        ByteBuf content = packet.content();
        content.writeByte(0x80);
        content.writeMedium(ctr++);
        content.writeByte(0x00);

        DatagramPacket ePacket = encode(new DatagramPacket(Unpooled.buffer(), address));
        ByteBuf filledContent = encode(ePacket).content();
        int readableBytes = filledContent.readableBytes();
        content.writeShort(readableBytes*8);
        content.writeBytes(filledContent.readBytes(readableBytes).array());
        ctx.writeAndFlush(packet);
        return this;
    }
    */
}
