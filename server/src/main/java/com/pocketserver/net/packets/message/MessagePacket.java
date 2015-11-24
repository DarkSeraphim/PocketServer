package com.pocketserver.net.packets.message;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.util.PacketUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x85)
public class MessagePacket extends Packet {
    private String message;

    public MessagePacket(String message) {
        this.message = message;
    }

    public MessagePacket() {
    } // no-args for decoding

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        message = PacketUtils.readString(dg.content());
    }

    @Override
    public DatagramPacket encode(DatagramPacket dg) {
        dg.content().writeByte(getPacketID());
        PacketUtils.writeString(dg.content(), message);
        return dg;
    }

}
