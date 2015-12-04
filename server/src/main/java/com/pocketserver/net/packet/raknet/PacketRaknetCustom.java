package com.pocketserver.net.packet.raknet;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.IntStream;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.codec.Encapsulation;
import com.pocketserver.net.codec.EncapsulationStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketRaknetCustom extends Packet {
    private final List<EncapsulationStrategy> strategies;
    private ByteBuf content;
    private int count;

    public PacketRaknetCustom() {
        this.strategies = Lists.newArrayList();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) {
        out.add(new PacketRaknetAck(count));
        content.markReaderIndex();
        try {
            for (EncapsulationStrategy strategy : strategies) {
                strategy.decode(ctx, content, out);
            }
        } catch (Exception ex) {
            content.resetReaderIndex();
            Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to decode packet!", new Object[]{
                ex
            });
        } finally {
            content.release();
        }
    }

    @Override
    public void read(ByteBuf buf) {
        content = buf.copy();
        count = content.readMedium();
        Server.getServer().getLogger().trace(PocketLogging.Server.NETWORK, "Packet contains {} encapsulated packets", new Object[]{
            String.valueOf(count)
        });
        IntStream.range(0, count).forEachOrdered(value -> strategies.add(Encapsulation.fromId(content.readByte())));
    }
}
