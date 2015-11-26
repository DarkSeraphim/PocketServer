package com.pocketserver.net.packets.login;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x13)
public class ClientHandshakePacket extends Packet {

    @Override
    public void decode(ByteBuf content) {
        System.out.println("Client handshake.");
        content.readBytes(94);
    }
}
