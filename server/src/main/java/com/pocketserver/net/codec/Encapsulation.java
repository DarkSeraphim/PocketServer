package com.pocketserver.net.codec;

import com.google.common.base.Preconditions;

import java.net.InetSocketAddress;

import com.pocketserver.api.Server;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class Encapsulation {
    public static final Marker ENCAPSULATION_MARKER = MarkerFactory.getMarker("ENCAPSULATION");

    public static final EncapsulationStrategy BARE = (buf, ctx, remoteAddress) -> {
        Preconditions.checkArgument(buf.isReadable(), "unable to read from buf!");
        byte packetId = buf.readByte();
        Server.getServer().getLogger().debug(ENCAPSULATION_MARKER, "Decoding packet 0x{} using BARE strategy", String.format("%02x", packetId));
        Packet packet = PacketRegistry.construct(packetId);
        packet.setRemote(remoteAddress).read(buf);
        packet.handle(ctx);
    };

    public static final EncapsulationStrategy COUNT = (buf, ctx, remoteAddress) -> {
        buf.skipBytes(3);
        BARE.decode(buf, ctx, remoteAddress);
    };

    public static final EncapsulationStrategy COUNT_UNKNOWN = (buf, ctx, remoteAddress) -> {
        buf.skipBytes(4);
        COUNT.decode(buf, ctx, remoteAddress);
    };

    public static void decode(EncapsulationStrategy strategy, ByteBuf buf, ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
        try {
            strategy.decode(buf, ctx, remoteAddress);
        } catch (Exception ex) {
            Server.getServer().getLogger().error(ENCAPSULATION_MARKER, "Failed to decode packet!", ex);
        }
    }

    private Encapsulation() {
        throw new UnsupportedOperationException("Encapsulation cannot be instantiated!");
    }
}
