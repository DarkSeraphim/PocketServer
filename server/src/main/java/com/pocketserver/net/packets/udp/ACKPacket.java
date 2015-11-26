package com.pocketserver.net.packets.udp;

import com.google.common.base.Preconditions;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

@PacketID(0xC0)
public class ACKPacket extends Packet {
    private final int[] packets;

    public ACKPacket(int[] packets) {
        this.packets = packets;
    }

    //You shouldn't be initializing it like this.
    @SuppressWarnings("unused") //This will be used through reflection for receiving.
    public ACKPacket() {
        this(null);
    }

    @Override
    public void decode(ByteBuf content) {
        //Don't do anything. No need to respond/whatever.
    }

    @Override //Credit to jRakNet or whatever it's called. Very helpful. //TODO: Fix this up.
    public void encode(ByteBuf content) {
        Preconditions.checkNotNull(packets);

        ByteBuf payload = Unpooled.buffer(2048);
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
                        payload.writeByte((byte) 0x01);
                        payload.writeMedium(start);
                        start = last = current;
                    } else {
                        payload.writeByte((byte) 0x00);
                        payload.writeMedium(start);
                        payload.writeMedium(last);
                        start = last = current;
                    }
                    records = records + 1;
                }
            }

            if (start == last) {
                payload.writeByte((byte) 0x01);
                payload.writeMedium(start);
            } else {
                payload.writeByte((byte) 0x00);
                payload.writeMedium(start);
                payload.writeMedium(last);
            }
            records = records + 1;
        }
        content.writeByte(getPacketID());
        content.writeShort((short) records);
        content.writeBytes(payload);
    }
}