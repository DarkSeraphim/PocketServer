package com.pocketserver.net;

import com.google.common.base.MoreObjects;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
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

    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        throw new UnsupportedOperationException(String.format("%s#decode(DatagramPacket, ChannelHandlerContext) should be implemented.", getClass().getName()));
    }

    public DatagramPacket encode(DatagramPacket dg) {
        throw new UnsupportedOperationException(String.format("%s#encode(DatagramPacket) should be implemented.", getClass().getName()));
    }

    protected final void writeMagic(ByteBuf buf) {
        buf.writeLong(Protocol.MAGIC_1);
        buf.writeLong(Protocol.MAGIC_2);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Packet.class).add("id", id).toString();
    }
}
