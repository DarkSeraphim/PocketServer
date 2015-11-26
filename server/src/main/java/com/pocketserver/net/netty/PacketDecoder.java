package com.pocketserver.net.netty;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketManager;
import com.pocketserver.net.packets.udp.CustomPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

//TODO: Properly use decoders (thx connur)
public class PacketDecoder extends MessageToMessageDecoder<DatagramPacket> {
    /*
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte id = byteBuf.readByte();
        Packet packet;
        if (id <= (byte) 0x8F && id >= (byte) 0x80) {
            packet = new CustomPacket();
        } else {
            packet = PacketManager.getInstance().initializePacketById(id);
        }
        System.out.println("DECODING WORKS");
        list.add(packet);
    }
    */

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> list) throws Exception {
        ByteBuf content = packet.content();
        byte id = content.readByte();
        Packet mcpePacket;
        if (id <= (byte) 0x8F && id >= (byte) 0x80) {
            mcpePacket = new CustomPacket();
        } else {
            mcpePacket = PacketManager.getInstance().initializePacketById(id);
        }
        mcpePacket.setRemote(packet.sender());
        mcpePacket.decode(content);
        list.add(mcpePacket);
        System.out.println("everyday im decodin'.... da dda da da da da da daaa");
    }
}
