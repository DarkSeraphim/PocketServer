package com.pocketserver.net.packet.notify;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;

public class PacketNotifyNoFreeConnections extends Packet {

    public PacketNotifyNoFreeConnections() {
        super(PacketRegistry.PacketType.NO_FREE_CONNECTIONS);
    }

    @Override
    public void read(ByteBuf buf) throws Exception {
        // NOP
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        // NOP
    }
}
