package com.pocketserver.net.codec;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.pocketserver.exception.BadPacketException;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketHeader;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public enum Encapsulation implements EncapsulationStrategy {
    BARE(0x00) {
        // TODO: Move or get rid of
        // TODO: ^ Wait till Connor removes then make Mark agree with me ^
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out, short length) throws Exception {
            assertLength(length / 8, buf);
            byte packetId = buf.readByte();
            PacketHeader header = new PacketHeader(packetId);

            Packet packet = PacketRegistry.construct(packetId);
            packet.read(buf);
            packet.handle(ctx, out);

            if (header.isPair()) {
                byte b = buf.readByte();
                EncapsulationStrategy strategy = Encapsulation.fromId(b);
                short readShort = buf.readShort();
                strategy.decode(ctx, buf, out, readShort);
            }
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
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out, short length) throws Exception {
            assertLength(length / 8, buf);
            buf.skipBytes(3);
            BARE.decode(ctx, buf, out, length);
        }
    },
    COUNT_UNKNOWN(0x60) {
        private final Set<InetSocketAddress> receivedFrom = Sets.newSetFromMap(new MapMaker().weakKeys().makeMap());

        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out, short length) throws Exception {
            assertLength(length / 8, buf);
            InetSocketAddress address = ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).get();
            if (receivedFrom.add(address)) {
                buf.skipBytes(4);
            }
            COUNT.decode(ctx, buf, out, length);
        }
    };

    private final int id;

    Encapsulation(int id) {
        this.id = id;
    }

    public static EncapsulationStrategy fromId(int id) {
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

    public int getId() {
        return id;
    }

    void assertLength(int length, ByteBuf buf) {
        if (length > buf.readableBytes()) {
            throw new BadPacketException("The packets length can not be longer than the readable.");
        }
    }
}
