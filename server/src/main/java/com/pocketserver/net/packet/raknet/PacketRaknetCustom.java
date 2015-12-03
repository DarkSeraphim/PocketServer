package com.pocketserver.net.packet.raknet;

import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.codec.Encapsulation;
import com.pocketserver.net.codec.EncapsulationStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketRaknetCustom extends Packet {
    private EncapsulationStrategy strategy;
    private ByteBuf content;
    private int count;

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) {
        out.add(new PacketRaknetAck(count));
        try {
            content.markReaderIndex();
            strategy.decode(ctx, content, out);
        } catch (Exception ex) {
            content.resetReaderIndex();
            Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to decode packet!", ex);
        } finally {
            content.release();
        }
    }

    @Override
    public void read(ByteBuf buf) {
        content = buf.copy();
        count = content.readMedium();
        byte id = content.readByte();
        strategy = Encapsulation.fromId(id);
        content.readShort();
    }
}
