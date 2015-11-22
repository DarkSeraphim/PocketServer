package com.pocketserver.impl.net.netty;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketManager;

import com.pocketserver.impl.net.packets.udp.CustomPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class PocketServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf buf = msg.content();
        byte id = buf.readByte();
        String sid = String.format("%X", id);
        System.out.format("PacketID received: 0x%s\n", sid.length() == 1 ? "0" + sid : sid);
        Packet packet;
        if (id <= (byte) 0x8F && id >= (byte) 0x80) {
            packet = new CustomPacket();
        } else {
            packet = PacketManager.getInstance().initializePacketById(id);
        }
        if (packet != null) { // if null, then there's no packet with that id!
            //  System.out.println("Received " + packet.getClass().getSimpleName());
            packet.decode(msg, ctx);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

}
