package com.pocketserver.impl.net.packets.data;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x1A)
public class IPRecentlyConnected extends Packet {
    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        System.out.println("Interesting!");
    }

    @Override
    public DatagramPacket encode(DatagramPacket dg) {
        return null;
    }
}