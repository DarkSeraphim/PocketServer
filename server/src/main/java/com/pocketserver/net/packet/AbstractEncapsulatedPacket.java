package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

// TODO: Implement handling in encoder
public abstract class AbstractEncapsulatedPacket extends Packet {
    private static int counter = -1;

    @Override
    public abstract void write(ByteBuf buf) throws Exception;
}
