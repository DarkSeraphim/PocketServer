package com.pocketserver.net.packet;

import com.pocketserver.net.Packet;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketRaknetOpenConnectionReplyB extends Packet {
    private final short mtu;

    public PacketRaknetOpenConnectionReplyB(short mtu) {
        this.mtu = mtu;
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        writeMagic(buf);
        buf.writeLong(Protocol.SERVER_ID);
        buf.writeShort(getRemote().getPort());
        buf.writeShort(mtu);
        buf.writeByte(0);
    }
}
