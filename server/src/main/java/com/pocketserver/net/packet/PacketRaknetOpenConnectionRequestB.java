package com.pocketserver.net.packet;

import java.util.Optional;

import com.pocketserver.net.Packet;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketRaknetOpenConnectionRequestB extends Packet {
    private long clientId;
    private int cookie;
    private short port;
    private short mtu;
    private byte sec;

    @Override
    public void read(ByteBuf buf) throws Exception {
        if (buf.readLong() == Protocol.MAGIC_1 && buf.readLong() == Protocol.MAGIC_2) {
            sec = buf.readByte();
            cookie = buf.readInt();
            port = buf.readShort();
            mtu = buf.readShort();
            clientId = buf.readLong();
        }
    }

    @Override
    public Optional<Packet> handle(ChannelHandlerContext ctx) throws Exception {
        return Optional.of(new PacketRaknetOpenConnectionReplyB(mtu).setRemote(getRemote()));
    }
}
