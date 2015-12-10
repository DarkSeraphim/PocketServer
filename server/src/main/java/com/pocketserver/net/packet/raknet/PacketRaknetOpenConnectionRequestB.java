package com.pocketserver.net.packet.raknet;

import java.util.List;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketRaknetOpenConnectionRequestB extends Packet {
    private long clientId;
    private int cookie;
    private short port;
    private short mtu;
    private byte sec;

    public PacketRaknetOpenConnectionRequestB() {
        super(PacketRegistry.DefaultPacketType.OPEN_CONNECTION_REQUEST_B);
    }

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
    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {
        out.add(new PacketRaknetOpenConnectionReplyB(mtu, port));
    }
}
