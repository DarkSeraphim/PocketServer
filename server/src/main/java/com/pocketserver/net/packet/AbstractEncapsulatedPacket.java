package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

// TODO: Implement handling in encoder
public abstract class AbstractEncapsulatedPacket extends Packet {
    @Override
    public abstract void write(ByteBuf buf);
}
