package com.pocketserver.net.codec;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public enum Encapsulation implements EncapsulationStrategy {
    BARE(0x00) {
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
            short length = buf.readShort();
            byte packetId = buf.readByte();

            Packet packet = PacketRegistry.construct(packetId);
            packet.read(buf.copy());

            buf.skipBytes(length / 8);

            packet.handle(ctx, out);

        }

        @Override
        public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
            ByteBuf out = ctx.alloc().buffer(buf.readableBytes() + 7);
            {
                out.writeByte(0x80);
                out.writeMedium(counter.getAndIncrement());
                out.writeByte(0x00);
                out.writeShort(buf.readableBytes() * 8);
                out.writeBytes(buf);
            }
            return out;
        }
    },
    COUNT(0x40) {
        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
            buf.skipBytes(3);
            BARE.decode(ctx, buf, out);
        }
    },
    COUNT_UNKNOWN(0x60) {
        private final Set<InetSocketAddress> receivedFrom = Sets.newSetFromMap(new MapMaker().weakKeys().makeMap());

        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
            InetSocketAddress address = ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).get();
            if (receivedFrom.add(address)) {
                buf.skipBytes(4);
            }
            COUNT.decode(ctx, buf, out);
        }
    };

    private final byte id;

    Encapsulation(int id) {
        this.id = (byte) id;
    }

    @Override
    public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("cannot encode packets using this strategy");
    }

    public static EncapsulationStrategy fromId(byte id) {
        for (Encapsulation strategy : values()) {
            if (strategy.id == id) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Unsupported EncapsulationStrategy");
    }
}
