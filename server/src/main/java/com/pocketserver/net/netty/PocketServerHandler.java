package com.pocketserver.net.netty;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PocketServerHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
        packet.handle(ctx).ifPresent(outbound -> {
            Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Sending 0x{} to {}", String.format("%02x", PacketRegistry.getId(outbound)),  outbound.getRemote());
            ctx.writeAndFlush(outbound);
        });
    }
}
