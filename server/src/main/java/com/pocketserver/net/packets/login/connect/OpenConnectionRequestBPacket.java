package com.pocketserver.net.packets.login.connect;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;

import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x07)
public class OpenConnectionRequestBPacket extends Packet {
    long clientId;
    int cookie;
    short port;
    short mtu;
    byte sec;

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        ByteBuf buf = dg.content();
        if (buf.readLong() == Protocol.MAGIC_1 && buf.readLong() == Protocol.MAGIC_2) {
            sec = buf.readByte();
            cookie = buf.readInt();
            port = buf.readShort();
            mtu = buf.readShort();
            clientId = buf.readLong();

            new OpenConnectionReplyBPacket(mtu, dg.sender().getPort()).sendPacket(ctx);
        }
    }
}
