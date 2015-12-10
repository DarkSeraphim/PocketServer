package com.pocketserver.net.packet.play;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;

public class PacketPlaySpawnExperience extends Packet {
    private final long entityId;
    private final int amount;
    private final int x;
    private final int y;
    private final int z;

    public PacketPlaySpawnExperience(long entityId, int amount, int x, int y, int z) {
        super(PacketRegistry.DefaultPacketType.SPAWN_EXPERIENCE);
        this.entityId = entityId;
        this.amount = amount;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeLong(entityId);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(amount);
    }
}
