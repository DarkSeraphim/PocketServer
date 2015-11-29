package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import com.pocketserver.net.codec.EncapsulationStrategy;
import io.netty.buffer.ByteBuf;

public abstract class AbstractEncapsulatedPacket extends Packet {
    public abstract EncapsulationStrategy getEncapsulationStrategy();

    @Override
    public abstract void write(ByteBuf buf) throws Exception;
}
