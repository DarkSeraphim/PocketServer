package com.pocketserver.net.packet.raknet;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketRaknetNack extends Packet {
    @Override
    public void read(ByteBuf buf) {
        // NOP
    }
}
