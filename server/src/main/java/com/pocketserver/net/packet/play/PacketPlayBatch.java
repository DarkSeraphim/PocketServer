package com.pocketserver.net.packet.play;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;

public class PacketPlayBatch extends Packet {
    private final byte[] payload;

    public PacketPlayBatch(byte[] payload) {
        super(PacketRegistry.DefaultPacketType.BATCH);
        this.payload = payload;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeInt(payload.length);
        buf.writeBytes(payload);
    }
}
