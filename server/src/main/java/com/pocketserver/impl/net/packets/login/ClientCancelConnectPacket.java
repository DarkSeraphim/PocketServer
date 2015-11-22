package com.pocketserver.impl.net.packets.login;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.PacketID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x15)
public class ClientCancelConnectPacket extends InPacket {

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        System.out.println(dg.sender().getHostName() + " has cancelled the login.");
    }
}
