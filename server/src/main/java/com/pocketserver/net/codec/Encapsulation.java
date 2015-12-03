package com.pocketserver.net.codec;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.pocketserver.exception.BadPacketException;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public enum Encapsulation implements EncapsulationStrategy {
    BARE(0x00) {
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
            int packetLength = buf.readShort() / 8;

            if (buf.readableBytes() < packetLength) {
                throw new BadPacketException(String.format("Expecting packet with a length of %d, received packet with a length of %d instead", packetLength, buf.readableBytes()));
            }

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
                out.writeShort(buf.readableBytes() * 8);
                out.writeBytes(buf);
            }
            return out;
        }
    },
    COUNT(0x40, BARE, 3),
    COUNT_UNKNOWN(0x60) {
        private volatile boolean hasReceived = false;

        @Override
        public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
            int packetLength = buf.readShort() / 8;
            buf.skipBytes(3);
            if (!hasReceived) {
                buf.skipBytes(4);
                hasReceived = true;
            }

            if (buf.readableBytes() < packetLength) {
                throw new BadPacketException(String.format("Expecting packet with a length of %d, received packet with a length of %d instead", packetLength, buf.readableBytes()));
            }
            Encapsulation.BARE.decode(ctx, buf, out);
        }
    };

    private final EncapsulationStrategy delegate;
    private final int skip;
    private final int id;

    Encapsulation(int id) {
        this(id, null, 0);
    }

    Encapsulation(int id, EncapsulationStrategy delegate, int skip) {
        this.id = id;
        this.delegate = delegate;
        this.skip = skip;
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
    public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
        if (this.delegate == null) {
            throw new IllegalStateException(name() + " does neither override decode nor provide a delegate");
        }
        buf.skipBytes(skip);
        delegate.decode(ctx, buf, out);
    }

    @Override
    public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("cannot encode packets using this strategy");
    }
}
