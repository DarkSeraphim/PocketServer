package com.pocketserver.impl.net.netty;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class PocketServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    int ctr = 0;
    int secondCtr = 0;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        //System.out.println("Sender: " + msg.sender().getAddress().getHostAddress());
        ByteBuf buf = msg.content();
        byte id = buf.readByte();
        String sid = String.format("%X", id);
        System.out.format(ctr++ + " = PacketID received: 0x%s\n", sid.length() == 1 ? "0" + sid : sid);
        Packet packet = PacketManager.getInstance().initializePacketById(id);
        if (packet != null) { // if null, then there's no packet with that id!
            //  System.out.println("Received " + packet.getClass().getSimpleName());
            packet.decode(msg, ctx);
        }
        System.out.println(msg.content().readableBytes());
        System.out.println(buf.readableBytes());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println(secondCtr++ + "Yes.");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

}
