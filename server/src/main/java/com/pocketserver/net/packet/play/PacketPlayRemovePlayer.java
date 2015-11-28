package com.pocketserver.net.packet.play;

import java.util.UUID;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayRemovePlayer extends Packet {
    private final long entityId;
    private final UUID uniqueId;

    public PacketPlayRemovePlayer(long entityId, UUID uniqueId) {
        this.entityId = entityId;
        this.uniqueId = uniqueId;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeLong(entityId);
        buf.writeLong(uniqueId.getMostSignificantBits());
        buf.writeLong(uniqueId.getLeastSignificantBits());
    }
}
