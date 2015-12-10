package com.pocketserver.net.packet.connect;

import java.util.List;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketConnectOpenRequest extends Packet {
    private long timestamp;
    private long clientId;
    private byte sec;

    public PacketConnectOpenRequest() {
        super(PacketRegistry.PacketType.OPEN_REQUEST);
    }

    @Override
    public void read(ByteBuf buf) throws Exception {
        clientId = buf.readLong();
        timestamp = buf.readLong();
        sec = buf.readByte();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {
        out.add(new PacketConnectOpenResponse(timestamp, ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).get()));
    }
}
