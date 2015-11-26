package com.pocketserver.net.packets.login.connect;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;

import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x05)
public class OpenConnectionRequestAPacket extends Packet {
    private byte proto;
    private int mtu;

    @Override
    public void decode(ByteBuf content) {
        long magic1 = content.readLong();
        long magic2 = content.readLong();
        if (magic1 == Protocol.MAGIC_1 && magic2 == Protocol.MAGIC_2) {
            proto = content.readByte();
            mtu = content.readableBytes();
        }
    }

    @Override
    public void handlePacket(Channel channel) {
        Packet packet;
        if (proto == Protocol.RAKNET_VERSION) {
            packet = new OpenConnectionReplyAPacket(mtu);
        } else {
            packet = new IncompatibleProtocolPacket();
        }
        packet.setRemote(getRemote());
        packet.sendPacket(channel);
    }
}