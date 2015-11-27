package com.pocketserver.net.codec;

import com.google.common.base.Preconditions;

import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public final class Encapsulation {
    public static final EncapsulationStrategy BARE = (buf, ctx, out) -> {
        Preconditions.checkArgument(buf.isReadable(), "unable to read from buf!");
        byte packetId = buf.readByte();
        Packet packet = PacketRegistry.construct(packetId);
        packet.read(buf);
        packet.handle(ctx, out);
    };

    public static final EncapsulationStrategy COUNT = (buf, ctx, out) -> {
        buf.skipBytes(3);
        BARE.decode(buf, ctx, out);
    };

    public static final EncapsulationStrategy COUNT_UNKNOWN = (buf, ctx, out) -> {
        buf.skipBytes(4);
        COUNT.decode(buf, ctx, out);
    };

    public static void decode(EncapsulationStrategy strategy, ByteBuf buf, ChannelHandlerContext ctx, List<Packet> out) {
        try {
            strategy.decode(buf, ctx, out);
        } catch (Exception ex) {
            Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to decode packet!", ex);
        }
    }

    private Encapsulation() {
        throw new UnsupportedOperationException("Encapsulation cannot be instantiated!");
    }
}
