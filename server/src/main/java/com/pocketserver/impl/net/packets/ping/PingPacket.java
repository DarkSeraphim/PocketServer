package com.pocketserver.impl.net.packets.ping;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.PacketID;
import com.pocketserver.impl.net.packets.oldudp.CustomPacketOld.EncapsulationStrategy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x00)
public class PingPacket extends InPacket {

    private long identifier;

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        identifier = dg.content().readLong();
        new PongPacket(identifier).sendPacket(ctx,dg.sender());
    }
}