package com.pocketserver.net.packets.udp;

import com.pocketserver.net.PacketID;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@SuppressWarnings("deprecation")
@PacketID(0xA0)
public class NACKPacket extends ACKPacket {
    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        super.decode(dg, ctx);
    }
}
