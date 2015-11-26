package com.pocketserver.net.netty;

import com.pocketserver.api.Server;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PocketServerHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
        packet.handle(ctx).ifPresent(outbound -> {
            ctx.writeAndFlush(outbound).addListener(future -> {
                if (future.isSuccess()) {
                    Server.getServer().getLogger().debug(PipelineUtils.NETWORK_MARKER, "Sending 0x{} to {}", String.format("%02x", PacketRegistry.getId(outbound)),  outbound.getRemote());
                }
            });
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Server.getServer().getLogger().error(PipelineUtils.NETWORK_MARKER, "", cause);
    }
}
