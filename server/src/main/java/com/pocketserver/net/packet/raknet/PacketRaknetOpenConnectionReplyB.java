package com.pocketserver.net.packet.raknet;

import com.pocketserver.net.Packet;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketRaknetOpenConnectionReplyB extends Packet {
    private final short port;
    private final short mtu;

    public PacketRaknetOpenConnectionReplyB(short mtu, short port) {
        this.port = port;
        this.mtu = mtu;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        writeMagic(buf);
        buf.writeLong(Protocol.SERVER_ID);
        buf.writeShort(port);
        buf.writeShort(mtu);
        buf.writeByte(0);
    }
}
