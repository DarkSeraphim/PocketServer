package com.pocketserver.net.packet.raknet;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketRaknetIncompatibleProtocol extends Packet {

    public PacketRaknetIncompatibleProtocol() {
        super(PacketRegistry.PacketType.INCOMPATIBLE_PROTOCOL);
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        buf.writeByte(Protocol.RAKNET_VERSION);
        writeMagic(buf);
        buf.writeLong(Protocol.SERVER_ID);
    }
}
