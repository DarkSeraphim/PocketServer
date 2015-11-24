package com.pocketserver.impl.net;

import com.google.common.base.MoreObjects;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
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

    @Deprecated
    public Packet sendPacket(ChannelHandlerContext ctx) {
        return this.sendPacket(ctx.channel());
    }

    public Packet sendPacket(Channel channel) {
        channel.writeAndFlush(this);
        return this;
    }

    public void handlePacket(Channel channel) {
        throw new UnsupportedOperationException(String.format("%s#handlePacket() should be implemented.", getClass().getName()));
    }

    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        throw new UnsupportedOperationException(String.format("%s#decode(DatagramPacket, ChannelHandlerContext) should be implemented.", getClass().getName()));
    }

    public DatagramPacket encode(DatagramPacket dg) {
        throw new UnsupportedOperationException(String.format("%s#encode(DatagramPacket) should be implemented.", getClass().getName()));
    }

    public void decode(ByteBuf content) {
        throw new UnsupportedOperationException(String.format("%s#decode(InetAddress, ByteBuf) should be implemented.", getClass().getName()));
    }

    public byte[] encode(SocketAddress address) {
        return encode(new DatagramPacket(Unpooled.buffer(), (InetSocketAddress) address)).content().array();
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
