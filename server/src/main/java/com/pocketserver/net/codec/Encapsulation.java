package com.pocketserver.net.codec;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.pocketserver.exception.BadPacketException;
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
            assertLength(buf);
            decode0(ctx, buf, out);
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
            assertLength(buf);
            buf.skipBytes(3);
            decode0(ctx, buf, out);
        }
    },
    COUNT_UNKNOWN(0x60) {
        private Set<InetSocketAddress> receivedFrom = Sets.newSetFromMap(new MapMaker().weakKeys().makeMap());

        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
            InetSocketAddress address = ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).get();

            assertLength(buf);
            buf.skipBytes(3);
            if (receivedFrom.add(address)) {
                buf.skipBytes(4);
            }
            decode0(ctx, buf, out);
        }
    };

    private final int id;

    Encapsulation(int id) {
        this.id = id;
    }

    public static EncapsulationStrategy fromId(byte id) {
        for (Encapsulation strategy : values()) {
            if (strategy.id == id) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Unsupported EncapsulationStrategy");
    }

    @Override
    public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("cannot encode packets using this strategy");
    }

    void decode0(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
        byte packetId = buf.readByte();
        Packet packet = PacketRegistry.construct(packetId);
        packet.read(buf);
        packet.handle(ctx, out);
    }

    void assertLength(ByteBuf buf) {
        int packetLength = buf.readShort() / 8;
        if (packetLength > buf.readableBytes()) {
            throw new BadPacketException(String.format("Expecting packet with a length of %d, received packet with a length of %d instead", packetLength, buf.readableBytes()));
        }
    }
}
