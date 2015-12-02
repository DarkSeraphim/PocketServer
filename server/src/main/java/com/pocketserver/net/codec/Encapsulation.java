package com.pocketserver.net.codec;

import com.google.common.base.Preconditions;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public enum Encapsulation implements EncapsulationStrategy {
    BARE((byte) 0x00) {
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
                out.writeShort(buf.readableBytes() * 8);
                out.writeBytes(buf);
            }
            return out;
        }
    },
    COUNT ((byte) 0x40, BARE, 3),
    COUNT_UNKNOWN ((byte) 0x60, COUNT, 4);

    private final byte id;

    private final EncapsulationStrategy delegate;

    private final int skip;

    Encapsulation(byte id) {
        this(id, null, 0);
    }

    Encapsulation(byte id, EncapsulationStrategy delegate, int skip) {
        this.id = id;
        this.delegate = delegate;
        this.skip = skip;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Packet> out) throws Exception {
        if (this.delegate == null) {
            throw new IllegalStateException(name() + " does neither override decode nor provide a delegate");
        }
        buf.skipBytes(this.skip);
        this.delegate.decode(ctx, buf, out);
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
        return null;
    }
}
