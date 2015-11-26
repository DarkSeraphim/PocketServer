package com.pocketserver.net.packets.data;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x1A)
public class IPRecentlyConnected extends Packet {

}
