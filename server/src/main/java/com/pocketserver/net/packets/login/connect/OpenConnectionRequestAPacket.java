package com.pocketserver.net.packets.login.connect;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;

import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x05)
public class OpenConnectionRequestAPacket extends Packet {
    private byte proto;
    private int mtu;

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        ByteBuf buf = dg.content();
        long magic1 = buf.readLong();
        long magic2 = buf.readLong();
        if (magic1 == Protocol.MAGIC_1 && magic2 == Protocol.MAGIC_2) {
            proto = buf.readByte();
            mtu = buf.readableBytes();
            if (proto == Protocol.RAKNET_VERSION) {
                new OpenConnectionReplyAPacket(mtu).sendPacket(ctx, dg.sender());
            } else {
                new IncompatibleProtocolPacket().sendPacket(ctx, dg.sender());
            }
        }
    }
}