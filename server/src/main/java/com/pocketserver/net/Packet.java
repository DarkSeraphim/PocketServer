package com.pocketserver.net;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class Packet {
    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {

    }

    public void write(ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("packet should implement write");
    }

    public void read(ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("packet should implement read");
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(Packet.class)
            .add("id", PacketRegistry.getId(this))
            .add("type", getClass().getSimpleName())
            .toString();
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
