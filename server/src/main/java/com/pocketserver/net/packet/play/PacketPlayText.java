package com.pocketserver.net.packet.play;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;

public class PacketPlayText extends Packet {
    private final byte type;

    public PacketPlayText(byte type) {
        super(PacketRegistry.PacketType.TEXT);
        this.type = type;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeByte(type);
    }
}
