package com.pocketserver.impl.net.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class PipelineInitializer extends ChannelInitializer<NioDatagramChannel> {
    @Override
    protected void initChannel(NioDatagramChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        //pipeline.addLast(new PacketEncoder());
        //pipeline.addLast(new PacketDecoder());

        pipeline.addLast(new PocketServerHandler());
    }
}
