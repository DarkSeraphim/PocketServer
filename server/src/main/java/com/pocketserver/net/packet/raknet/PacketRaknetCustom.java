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
    private EncapsulationStrategy encapsulationStrategy;
    private ByteBuf content;
    private int count;

    @Override
    public void read(ByteBuf buf) {
        content = buf.copy();
        count = content.readMedium();
        byte id = content.readByte();
        encapsulationStrategy = Encapsulation.fromId(id);
        if (encapsulationStrategy == null) {
            Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Unhandled EncapsulationStrategy: 0x{}", String.format("%02x", id));
            // Consider throwing an error?
        }
        content.readShort();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) {
        out.add(new PacketRaknetAck(count));
        try {
            encapsulationStrategy.decode(ctx, content, out);
        } catch (Exception ex) {
            Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to decode packet!", ex);
        }
        content.release();
    }
}
