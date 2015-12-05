package com.pocketserver.net.netty;

import java.net.InetSocketAddress;
import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import com.pocketserver.net.packet.Encapsulated;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

public class PacketEncoder extends MessageToMessageEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        InetSocketAddress recipient = ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).get();
        ByteBuf buf = ctx.alloc().buffer();
        try {
            byte id = PacketRegistry.getId(msg);
            buf.writeByte(id);
            msg.write(buf);
            if (msg instanceof Encapsulated) {
                buf.markWriterIndex();
                try {
                    Encapsulated encapsulated = (Encapsulated) msg;
                    buf = encapsulated.getEncapsulationStrategy().encode(ctx, buf);
                } catch (Exception ex) {
                    Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to encode packet!", ex);
                    buf.resetWriterIndex();
                }
            }
            out.add(new DatagramPacket(buf, recipient));
        } finally {
            msg.close();
        }
    }
}
