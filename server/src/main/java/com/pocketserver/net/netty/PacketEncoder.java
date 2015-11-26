package com.pocketserver.net.netty;

import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

public class PacketEncoder extends MessageToMessageEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        byte id = PacketRegistry.getId(msg);
        Server.getServer().getLogger().debug(PipelineUtils.NETWORK_MARKER, "Sending 0x{} to {}", String.format("%02x", id),  msg.getRemote());
        buf.writeByte(id);
        msg.write(buf);
        out.add(new DatagramPacket(buf, msg.getRemote()));
    }
}
