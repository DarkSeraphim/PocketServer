package com.pocketserver.net.packet.play;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayRemoveEntity extends Packet {
    private final long entityId;

    public PacketPlayRemoveEntity(long entityId) {
        this.entityId = entityId;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeLong(entityId);
    }
}
