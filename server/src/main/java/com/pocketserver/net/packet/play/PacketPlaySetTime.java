package com.pocketserver.net.packet.play;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlaySetTime extends Packet {
    private final byte started;
    private final int time;

    public PacketPlaySetTime(byte started, int time) {
        this.started = started;
        this.time = time;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeInt(time);
        buf.writeByte(started);
    }
}
