package com.pocketserver.net.packet.play;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;

public class PacketPlayRemoveEntity extends Packet {
    private final long entityId;

    public PacketPlayRemoveEntity(long entityId) {
        super(PacketRegistry.DefaultPacketType.REMOVE_ENTITY);
        this.entityId = entityId;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeLong(entityId);
    }
}
