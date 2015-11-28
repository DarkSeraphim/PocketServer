package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public abstract class AbstractEncapsulatedPacket extends Packet {
    @Override
    public abstract void write(ByteBuf buf) throws Exception;
}
