package com.pocketserver.net.packet.notify;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketNotifyNoFreeConnections extends Packet {
    @Override
    public void read(ByteBuf buf) throws Exception {
        // NOP
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        // NOP
    }
}
