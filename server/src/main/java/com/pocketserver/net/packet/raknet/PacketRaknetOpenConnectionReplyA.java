package com.pocketserver.net.packet.raknet;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketRaknetOpenConnectionReplyA extends Packet {
    private final short mtu;

    public PacketRaknetOpenConnectionReplyA(short mtu) {
        super(PacketRegistry.PacketType.OPEN_CONNECTION_REPLY_A);
        this.mtu = mtu;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        writeMagic(buf);
        buf.writeLong(Protocol.SERVER_ID);
        buf.writeByte(0);
        buf.writeShort(mtu);
    }
}
