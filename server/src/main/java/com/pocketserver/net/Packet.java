package com.pocketserver.net;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

public abstract class Packet {
    private final int id;
    private InetSocketAddress remote;

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
        Preconditions.checkArgument(channel.isWritable());
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

    public void encode(ByteBuf content) {
        throw new UnsupportedOperationException(getClass().getSimpleName() +
                "#decode(ByteBuf, SocketAddress) should be implemented.");
    }

    protected final void writeMagic(ByteBuf buf) {
        buf.writeLong(Protocol.MAGIC_1);
        buf.writeLong(Protocol.MAGIC_2);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Packet.class).add("id", id).toString();
    }

    public DatagramPacket createDatagram(int pool) {
        return new DatagramPacket(Unpooled.buffer(pool), remote);
    }

    public final InetSocketAddress getRemote() {
        return remote;
    }

    public void setRemote(InetSocketAddress remote) {
        this.remote = remote;
    }
}
