package com.pocketserver.net.packet.raknet;

import com.pocketserver.net.Packet;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketRaknetIncompatibleProtocol extends Packet {
    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeByte(Protocol.RAKNET_VERSION);
        writeMagic(buf);
        buf.writeLong(Protocol.SERVER_ID);
    }
}
