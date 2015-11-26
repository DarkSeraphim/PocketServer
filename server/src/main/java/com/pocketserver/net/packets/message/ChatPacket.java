package com.pocketserver.net.packets.message;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.util.PacketUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0xB5)
public class ChatPacket extends Packet {

    @Override
    public void decode(ByteBuf content) {
        String message = PacketUtils.readString(content);
    }
}
