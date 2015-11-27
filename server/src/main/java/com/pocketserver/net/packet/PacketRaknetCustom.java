package com.pocketserver.net.packet;

import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
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
    public void handle(ChannelHandlerContext ctx, List<Packet> out) {
        out.add(new PacketRaknetAck(count));
        switch (encapsulationMethod) {
            case 0x00:
                Encapsulation.decode(Encapsulation.BARE, content, ctx, out);
                break;
            case 0x40:
                Encapsulation.decode(Encapsulation.COUNT, content, ctx, out);
                break;
            case 0x60:
                Encapsulation.decode(Encapsulation.COUNT_UNKNOWN, content, ctx, out);
                break;
            default:
                Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Unhandled EncapsulationStrategy: 0x{}", String.format("%02x", encapsulationMethod));
                break;
        }
        content.release();
    }
}
