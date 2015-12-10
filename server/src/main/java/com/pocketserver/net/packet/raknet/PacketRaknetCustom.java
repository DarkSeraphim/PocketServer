package com.pocketserver.net.packet.raknet;

import java.util.Arrays;
import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.codec.Encapsulation;
import com.pocketserver.net.codec.EncapsulationStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketRaknetCustom extends Packet {
    private ByteBuf content;
    private int count;

    public PacketRaknetCustom() {
        super(PacketRegistry.PacketType.CUSTOM);
    }

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {
        out.add(new PacketRaknetAck(count));
        try {
            byte strategyId = content.readByte();
            short packetLength = content.readShort();
            Server.getServer().getLogger().trace("Encapsulation ID: 0x{}", String.format("%02x", strategyId));
            Server.getServer().getLogger().trace("Encapsulation Bit Length: {}", packetLength);
            try {
                EncapsulationStrategy strategy = Encapsulation.fromId(strategyId);
                Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Decoding packet with strategy {}", new Object[]{
                    Arrays.stream(Encapsulation.class.getEnumConstants()).filter(s -> s.getId() == strategyId).findFirst().map(Encapsulation::name).get()
                });
                strategy.decode(ctx, content, out, packetLength);
            } catch (Exception cause) {
                Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to decode packet", new Object[]{
                    cause
                });
            }
        } finally {
            content.release();
        }
    }

    @Override
    public void read(ByteBuf buf) {
        content = buf.copy();
        count = content.readMedium();
    }
}
