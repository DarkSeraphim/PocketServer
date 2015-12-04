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
            for (int i = 0; i < count; i++) {
                // diediedie.
                EncapsulationStrategy strategy = Encapsulation.fromId(content.readByte());
                try {
                    strategy.decode(ctx, content, out);
                } catch (Exception cause) {
                    Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to code packet {}/{}", new Object[]{
                        String.valueOf(i),
                        String.valueOf(count),
                        cause
                    });
                    // PANIC
                    content.skipBytes(content.readableBytes());
                    break;
                }
            }
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
    }
}
