package com.pocketserver.impl.net.netty;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class PocketServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        //System.out.println("Sender: " + msg.sender().getAddress().getHostAddress());
        ByteBuf buf = msg.content();
        byte id = buf.readByte();
        String sid = String.format("%X", id);
        System.out.format("PacketID received: 0x%s\n", sid.length() == 1 ? "0" + sid : sid);
        if (id == (byte)0x00) System.out.println("Yeahh..... shoot.");
        Packet packet = PacketManager.getInstance().initializePacketById(id);
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
