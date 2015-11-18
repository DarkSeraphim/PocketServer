package com.pocketserver.impl.net.packets.data;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.PacketID;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@DataPacket
@PacketID(0x8F)
public class LoginInfoPacket extends InPacket {
    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        System.out.println("LoginInfoPacket!!! woooooohooooo");
        ByteBuf content = dg.content();
        String name = readString(content);
        System.out.println("Username: " + name);
    }
}
