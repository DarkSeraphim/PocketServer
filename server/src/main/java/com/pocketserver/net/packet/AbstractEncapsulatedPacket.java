package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

// TODO: Implement handling in encoder
public abstract class AbstractEncapsulatedPacket extends Packet {
    private static int counter = -1;

    @Override
    public abstract void write(ByteBuf buf) throws Exception;

    public ByteBuf writeEncapsulation(ByteBuf buf) {
        int length = buf.readableBytes();
        int totalLength = length+7;
        ByteBuf outer = Unpooled.buffer(totalLength, totalLength);
        {
            outer.writeByte(0x80);
            outer.writeMedium(counter++);
            outer.writeByte(0x00);
            outer.writeShort(length);
            outer.writeBytes(buf);
        }
        return outer;
    }
}
