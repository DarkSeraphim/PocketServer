package com.pocketserver.net.packet;

import com.google.common.base.Preconditions;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PacketRaknetAck extends Packet {
    private final int[] packets;

    public PacketRaknetAck(int... packets) {
        Preconditions.checkArgument(packets.length > 0, "number of acknowledged packets must be greater than zero");
        this.packets = packets;
    }

    public PacketRaknetAck() {
        this.packets = new int[0];
    }

    @Override
    public void write(ByteBuf buf) {
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
        buf.writeShort(records);
        buf.writeBytes(payload);
    }
}
