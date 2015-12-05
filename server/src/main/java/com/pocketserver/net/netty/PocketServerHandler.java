package com.pocketserver.net.netty;

import com.google.common.collect.Lists;

import java.net.InetSocketAddress;
import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;

public class PocketServerHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
        Attribute<InetSocketAddress> addressAttribute = ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE);
        InetSocketAddress address = addressAttribute.get();
        try {
            List<Packet> out = Lists.newLinkedList();
            packet.record(this);
            packet.handle(ctx, out);
            packet.close();
            for (Packet outbound : out) {
                ctx.writeAndFlush(outbound).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Sent 0x{} to {}", new Object[]{
                                String.format("%02x", PacketRegistry.getId(outbound)).toUpperCase(),
                                address
                            });
                        } else {
                            Server.getServer().getLogger().error("Failed to send packet", future.cause());
                        }
                    }
                });
            }
        } finally {
            addressAttribute.remove();
        }
    }
}
