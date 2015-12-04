package com.pocketserver.net.netty;

import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

public class PacketDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).set(msg.sender());
        byte id = msg.content().readByte();
        Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Received 0x{} from {}", new Object[] {
            String.format("%02x", id).toUpperCase(),
            msg.sender()
        });
        Packet packet = PacketRegistry.construct(id);
        if (packet.getLeak() != null) {
            packet.getLeak().record(this);
        }
        packet.read(msg.content());
        out.add(packet);
    }
}
