package com.pocketserver.net.packet;

import java.util.Optional;

import com.pocketserver.net.Packet;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketRaknetOpenConnectionRequestA extends Packet {
    private byte raknetVersion;
    private int mtu;

    @Override
    public void read(ByteBuf buf) throws Exception {
        long magicOne = buf.readLong();
        long magicTwo = buf.readLong();

        if (magicOne == Protocol.MAGIC_1 && magicTwo == Protocol.MAGIC_2) {
            raknetVersion = buf.readByte();
            mtu = buf.readableBytes();
        }
    }

    @Override
    public Optional<Packet> handle(ChannelHandlerContext ctx) throws Exception {
        Packet packet;
        if (raknetVersion == Protocol.RAKNET_VERSION) {
            packet = new PacketRaknetOpenConnectionReplyA((short) mtu);
        } else {
            packet = new PacketRaknetIncompatibleProtocol();
        }
        return Optional.of(packet.setRemote(getRemote()));
    }
}
