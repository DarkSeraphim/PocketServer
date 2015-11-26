package com.pocketserver.net.packets.data.login;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.packets.data.DataPacket;
import com.pocketserver.net.util.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@DataPacket
@PacketID(0x82)
public class LoginInfoPacket extends Packet {

    @Override
    public void decode(ByteBuf content) {
        System.out.println("LoginInfoPacket!!! woooooohooooo");
        System.out.println(content.readableBytes());
        content.readByte();
        String name = PacketUtils.readString(content);
        System.out.println(name);
        content.resetReaderIndex();
        while (content.isReadable()) {
            System.out.print(content.readByte() + " ");
        }
    }
}
