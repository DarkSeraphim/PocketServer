package com.pocketserver.net.netty;

import com.google.common.collect.Lists;

import java.util.List;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PipelineUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PocketServerHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
        List<Packet> out = Lists.newLinkedList();
        if (packet.getLeak() != null) {
            packet.getLeak().record(this);
        }
        packet.handle(ctx, out);
        out.forEach(ctx::write);
        if (ctx.hasAttr(PipelineUtils.ADDRESS_ATTRIBUTE)) {
            ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).remove();
        }
        ctx.flush();
    }
}
