package com.pocketserver.net;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.net.InetSocketAddress;
import java.util.Optional;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class Packet {
    private InetSocketAddress remote;

    public Optional<Packet> handle(ChannelHandlerContext ctx) throws Exception {
        return Optional.empty();
    }

    public void write(ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("packet should implement write");
    }

    public void read(ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("packet should implement read");
    }

    public final InetSocketAddress getRemote() {
        return remote;
    }

    public final Packet setRemote(InetSocketAddress remote) {
        this.remote = remote;
        return this;
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(Packet.class).add("type", getClass().getSimpleName()).toString();
    }

    protected final void writeMagic(ByteBuf buf) {
        Preconditions.checkArgument(buf.isWritable(), "unable to write to buf");
        buf.writeLong(Protocol.MAGIC_1);
        buf.writeLong(Protocol.MAGIC_2);
    }

    protected final String readString(ByteBuf buf) {
        Preconditions.checkArgument(buf.isReadable(), "unable to read from buf");
        byte[] data = new byte[buf.readShort()];
        buf.readBytes(data);
        return new String(data, Charsets.UTF_8);
    }

    protected final void writeString(ByteBuf buf, String str) {
        Preconditions.checkArgument(buf.isWritable(), "unable to write to buf");
        str = Protocol.DISALLOWED_CHARS.matcher(str).replaceAll("");
        byte[] data = str.getBytes(Charsets.UTF_8);
        buf.writeShort(data.length);
        buf.writeBytes(data);
    }
}
