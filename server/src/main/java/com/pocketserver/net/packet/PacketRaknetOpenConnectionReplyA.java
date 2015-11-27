package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketRaknetOpenConnectionReplyA extends Packet {
    private final short mtu;

    public PacketRaknetOpenConnectionReplyA(short mtu) {
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