package com.pocketserver.impl.net.packets.login.connect;

import com.google.common.base.Preconditions;
import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x01)
public class UnconnectedPingPacket extends Packet {

    private long identifier = -1;

    @Override
    public void decode(ByteBuf content) {
        this.identifier = content.readLong();
        content.readLong();
        content.readLong();
    }

    @Override
    public void handlePacket(Channel channel) {
        Preconditions.checkArgument(identifier != -1);
        UnconnectedPongPacket packet = new UnconnectedPongPacket(0x1C, identifier);
        channel.flush();
        packet.sendPacket(channel);
    }
}