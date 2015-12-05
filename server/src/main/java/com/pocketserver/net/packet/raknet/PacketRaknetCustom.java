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
    private ByteBuf content;
    private int count;

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {
        out.add(new PacketRaknetAck(count));
        try {

            while (content.isReadable()) {
                EncapsulationStrategy strategy = Encapsulation.fromId(content.readByte());
                try {
                    Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Decoding strategy, " + strategy.getClass().getSimpleName());
                    strategy.decode(ctx, content, out);
                } catch (Exception cause) {
                    Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to decode packet {}",
                            String.valueOf(count),
                            cause);
                    break;
                }
                break; //TODO: Remove this so that it actually loops.
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
