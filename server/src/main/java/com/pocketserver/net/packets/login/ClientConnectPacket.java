package com.pocketserver.net.packets.login;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x09)
public class ClientConnectPacket extends Packet {
    private long clientId;
    private long session;

    @Override
    public void decode(ByteBuf content) {
        clientId = content.readLong();
        session = content.readLong();
    }

    @Override
    public void handlePacket(Channel channel) {
        ServerHandshakePacket packet = new ServerHandshakePacket(session);
        packet.setRemote(getRemote());
        packet.sendPacket(channel);
    }
}
