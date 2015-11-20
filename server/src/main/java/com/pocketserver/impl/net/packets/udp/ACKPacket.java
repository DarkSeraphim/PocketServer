package com.pocketserver.impl.net.packets.udp;

import java.net.InetSocketAddress;

import com.google.common.base.Preconditions;
import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0xC0)
public class ACKPacket extends Packet {
    private final int[] packets;

    public ACKPacket(int[] packets) {
        this.packets = packets;
    }

    @Deprecated //You shouldn't be initializing it like this.
    @SuppressWarnings("unused") //This will be used through reflection for receiving.
    public ACKPacket() {
        this(null);
    }

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
       // System.out.println("Received an acknowledge packet. Class: "
       //         + this.getClass().getSimpleName());
    }

    @Override //Credit to jRakNet or whatever it's called. Very helpful. //TODO: Fix this up.
    public DatagramPacket encode(DatagramPacket dg) {
        Preconditions.checkNotNull(packets);

        ByteBuf content = dg.content();
        int count = packets.length;
        int records = 0;

        if (count > 0) {
            int pointer = 0;
            int start = packets[0];
            int last = packets[0];

            while (pointer + 1 < count) {
                int current = packets[pointer++];
                int diff = current - last;
                if (diff == 1) {
                    last = current;
                } else if (diff > 1) {
                    if (start == last) {
                        content.writeByte((byte) 0x01);
                        content.writeMedium(start);
                        start = last = current;
                    } else {
                        content.writeByte((byte) 0x00);
                        content.writeMedium(start);
                        content.writeMedium(last);
                        start = last = current;
                    }
                    records = records + 1;
                }
            }

            if (start == last) {
                content.writeByte((byte) 0x01);
                content.writeMedium(start);
            } else {
                content.writeByte((byte) 0x00);
                content.writeMedium(start);
                content.writeMedium(last);
            }
            records = records + 1;
        }
        content.writeByte(getPacketID());
        content.writeShort((short) records);
        return dg;
    }

    @Override
    public Packet sendPacket(ChannelHandlerContext ctx, InetSocketAddress address) {
        //System.out.println("Actually being sent.");
        return super.sendPacket(ctx, address);
    }
}