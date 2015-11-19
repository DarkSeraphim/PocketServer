package com.pocketserver.impl.net.packets.data;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.PacketID;
import com.pocketserver.impl.net.util.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@DataPacket
@PacketID({0x82,0x8f})
public class LoginInfoPacket extends InPacket {
    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        System.out.println("LoginInfoPacket!!! woooooohooooo");
        System.out.println(dg.content().readableBytes());
        ByteBuf content = dg.content();
        String name = PacketUtils.readString(content);
        System.out.println("Username: " + name);
    }
}
