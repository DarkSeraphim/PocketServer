package com.pocketserver.net.netty;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

//TODO: Properly use decoders (thx connur)
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf byteBuf) throws Exception {
        Channel channel = ctx.channel();
        byteBuf.writeBytes(packet.encode(channel.remoteAddress()));
        ctx.writeAndFlush(byteBuf);

        System.out.println("ENCODING WORKS");
    }
}
