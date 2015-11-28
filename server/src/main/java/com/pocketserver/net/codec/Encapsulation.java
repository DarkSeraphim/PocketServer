package com.pocketserver.net.codec;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public final class Encapsulation {
    public static final EncapsulationStrategy BARE = new EncapsulationStrategy() {
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
            Preconditions.checkArgument(buf.isReadable(), "unable to read from buf!");
            byte packetId = buf.readByte();
            Packet packet = PacketRegistry.construct(packetId);
            packet.read(buf);
            packet.handle(ctx, out);
        }

        @Override
        public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
            ByteBuf out = ctx.alloc().buffer(buf.readableBytes() + 7);
            {
                out.writeByte(0x80);
                out.writeMedium(counter.getAndIncrement());
                out.writeByte(0x00);
                out.writeShort(buf.readableBytes());
                out.writeBytes(buf);
            }
            return out;
        }
    };

    public static final EncapsulationStrategy COUNT = new EncapsulationStrategy() {
        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
            buf.skipBytes(3);
            BARE.decode(ctx, buf, out);
        }

        @Override
        public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
            return Encapsulation.BARE.encode(ctx, buf);
        }
    };

    public static final EncapsulationStrategy COUNT_UNKNOWN = new EncapsulationStrategy() {
        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
            buf.skipBytes(4);
            COUNT.decode(ctx, buf, out);
        }

        @Override
        public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
            return Encapsulation.BARE.encode(ctx, buf);
        }
    };

    public static void decode(EncapsulationStrategy strategy, ByteBuf buf, ChannelHandlerContext ctx, List<Packet> out) {
        try {
            strategy.decode(ctx, buf, out);
        } catch (Exception ex) {
            Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to decode packet!", ex);
        }
    }

    public static ByteBuf encode(EncapsulationStrategy strategy, ChannelHandlerContext ctx, ByteBuf buf) {
        try {
            return strategy.encode(ctx, buf);
        } catch (Exception ex) {
            Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to encode packet!", ex);
            return buf;
        }
    }

    private Encapsulation() {
        throw new UnsupportedOperationException("Encapsulation cannot be instantiated!");
    }
}
