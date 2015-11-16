package com.pocketserver.impl.net;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import com.google.common.base.Preconditions;
import com.pocketserver.impl.net.packets.oldudp.CustomPacketOld;
import com.pocketserver.impl.net.packets.oldudp.CustomPacketOld.EncapsulationStrategy;

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


    public final void writeString(ByteBuf buf, String str) {
        Preconditions.checkNotNull(str, "Cannot write a null string.");
        str = Protocol.DISALLOWED_CHARS.matcher(str).replaceAll("");
        System.out.println(str);
        buf.writeShort(str.length());
        buf.writeBytes(str.getBytes(Charset.defaultCharset()));
    }

    public final String readString(ByteBuf buf) {
        short len = buf.readShort();
        byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        return new String(bytes, Charset.defaultCharset());
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
