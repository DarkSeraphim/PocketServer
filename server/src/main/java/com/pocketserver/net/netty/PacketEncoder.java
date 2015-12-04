package com.pocketserver.net.netty;

import com.google.common.base.Preconditions;

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
        Preconditions.checkArgument(ctx.hasAttr(PipelineUtils.ADDRESS_ATTRIBUTE), "ctx does not contain address attribute!");
        InetSocketAddress recipient = ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).get();
        ByteBuf buf = ctx.alloc().buffer();
        byte id = PacketRegistry.getId(msg);
        buf.writeByte(id);
        msg.write(buf);
        if (msg instanceof Encapsulated) {
            buf.markReaderIndex();
            try {
                buf = ((Encapsulated) msg).getEncapsulationStrategy().encode(ctx, buf);
            } catch (Exception ex) {
                Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "Failed to encode packet!", ex);
                // Should reset the buffer to the old state to prevent 'corrupt' buffers
                buf.resetReaderIndex();
            }
        }
        msg.close();
        out.add(new DatagramPacket(buf, recipient));
        Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Sent 0x{} to {}", new Object[] {
            String.format("%02x", id).toUpperCase(),
            recipient
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Failure in PacketEncoder", cause);
    }
}
