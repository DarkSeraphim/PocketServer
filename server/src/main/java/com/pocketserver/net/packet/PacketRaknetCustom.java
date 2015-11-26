package com.pocketserver.net.packet;

import java.util.Optional;

import com.pocketserver.api.Server;
import com.pocketserver.net.Packet;
import com.pocketserver.net.codec.Encapsulation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketRaknetCustom extends Packet {
    private byte encapsulationMethod;
    private ByteBuf content;
    private int count;

    @Override
    public void read(ByteBuf buf) {
        content = buf.copy();
        count = content.readMedium();
        encapsulationMethod = content.readByte();
        content.readShort();
    }

    @Override
    public Optional<Packet> handle(ChannelHandlerContext ctx) {
        try {
            return Optional.of(new PacketRaknetAck(count).setRemote(getRemote()));
        } finally {
            switch (encapsulationMethod) {
                case 0x00:
                    Encapsulation.decode(Encapsulation.BARE, content, ctx, getRemote());
                    break;
                case 0x40:
                    Encapsulation.decode(Encapsulation.COUNT, content, ctx, getRemote());
                    break;
                case 0x60:
                    Encapsulation.decode(Encapsulation.COUNT_UNKNOWN, content, ctx, getRemote());
                    break;
                default:
                    Server.getServer().getLogger().debug(Encapsulation.ENCAPSULATION_MARKER, "Unhandled EncapsulationStrategy: 0x{}", String.format("%02x", encapsulationMethod));
                    break;
            }
            content.release();
        }
    }
}
