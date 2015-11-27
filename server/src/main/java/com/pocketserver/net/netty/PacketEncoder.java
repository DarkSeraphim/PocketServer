package com.pocketserver.net.netty;

import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

public class PacketEncoder extends MessageToMessageEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        byte id = PacketRegistry.getId(msg);
        buf.writeByte(id);
        Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, msg.getClass().getSimpleName());
                        msg.write(buf);
        out.add(new DatagramPacket(buf, msg.getRemote()));
    }
}
