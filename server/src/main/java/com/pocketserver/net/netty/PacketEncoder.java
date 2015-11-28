package com.pocketserver.net.netty;

import com.google.common.base.Preconditions;

import java.net.InetSocketAddress;
import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PipelineUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

public class PacketEncoder extends MessageToMessageEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) {
        Preconditions.checkArgument(ctx.hasAttr(PipelineUtils.ADDRESS_ATTRIBUTE), "ctx does not contain address attribute!");
        InetSocketAddress recipient = ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).getAndRemove();
        ByteBuf buf = ctx.alloc().buffer();
        byte id = msg.getPacketId();
        buf.writeByte(id);
        msg.write(buf);
        out.add(new DatagramPacket(buf, recipient));
        Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Sent 0x{} to {}", new Object[] {
            String.format("%02x", id).toUpperCase(),
            recipient
        });
    }
}
