package com.pocketserver.impl.net.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.oio.OioDatagramChannel;

public class PipelineInitializer extends ChannelInitializer<OioDatagramChannel> {
    @Override
    protected void initChannel(OioDatagramChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("decoder", new PacketDecoder());
        pipeline.addLast("encoder", new PacketEncoder());

        pipeline.addLast("handler", new PocketServerHandler());
    }
}
