package com.pocketserver.impl.net;

import java.net.InetSocketAddress;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

public abstract class Packet {
    private final int id;

    protected Packet() {
        id = getClass().getAnnotation(PacketID.class).value()[0];
    }

    protected Packet(int id) {
        this.id = id;
    }

    public final int getPacketID() {
        return this.id;
    }

    public Packet sendPacket(ChannelHandlerContext ctx, InetSocketAddress address) {
        ctx.writeAndFlush(encode(new DatagramPacket(Unpooled.buffer(), address)));
        return this;
    }

    public abstract void decode(DatagramPacket dg, ChannelHandlerContext ctx);

    public abstract DatagramPacket encode(DatagramPacket dg);

    @Override
    public String toString() {
        return this.getPacketID() + " " + this.getClass().getName();
    }
}
