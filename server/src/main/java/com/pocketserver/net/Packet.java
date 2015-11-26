package com.pocketserver.net;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public abstract class Packet {
    private final int id;
    private InetSocketAddress remote;

    protected Packet() {
        id = getClass().getAnnotation(PacketID.class).value()[0];
    }

    public final int getPacketID() {
        return this.id;
    }

    public Packet sendPacket(Channel channel) {
        Preconditions.checkArgument(channel.isWritable());
        channel.writeAndFlush(this);
        return this;
    }

    public void handlePacket(Channel channel) {
        throw new UnsupportedOperationException(String.format(
                "%s#handlePacket() should be implemented.", getClass().getName()));
    }

    public void decode(ByteBuf content) {
        throw new UnsupportedOperationException(String.format(
                "%s#decode(ByteBuf) should be implemented.", getClass().getName()));
    }

    public void encode(ByteBuf content) {
        throw new UnsupportedOperationException(String.format(
                "%s#encode(ByteBuf) should be implemented.", getClass().getSimpleName()));
    }

    public final InetSocketAddress getRemote() {
        return remote;
    }

    public void setRemote(InetSocketAddress remote) {
        this.remote = remote;
    }

    public String toString() {
        return MoreObjects.toStringHelper(Packet.class).add("id", id).toString();
    }
}
