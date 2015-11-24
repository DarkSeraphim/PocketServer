package com.pocketserver.net.packets.ping;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.packets.data.DataPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@DataPacket
@PacketID(0x00)
public class PingPacket extends Packet {
    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        ByteBuf content = dg.content();
        long identifier = content.readLong();
        new PongPacket(identifier).sendPacket(ctx);
        //if (bytes != 0) {
        //    DatagramPacket packet = new DatagramPacket(content.readBytes(new byte[bytes]),dg.recipient(),dg.sender());
        //    new PingPacket().decode(packet,ctx);
        //    System.out.println("Nextttt " + bytes);
        //} else {
        //    System.out.println("wat");
        //}
    }
}