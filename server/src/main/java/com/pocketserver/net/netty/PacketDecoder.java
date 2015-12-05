package com.pocketserver.net.netty;

import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketHeader;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

public class PacketDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).set(msg.sender());
        byte packetId = msg.content().readByte();

        PacketHeader header = new PacketHeader(packetId);
        Server.getServer().getLogger().trace(header.toString());

        Packet packet = PacketRegistry.construct(packetId);

        Object[] params = new Object[]{
            null,
            msg.sender()
        };

        if (Server.getServer().getLogger().isTraceEnabled()) {
            params[0] = packet.toString();
        } else {
            params[0] = String.format("0x%02X", packetId);
        }
        Server.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Received 0x{} from {}", params);
        packet.record(this);
        try {
            packet.read(msg.content());
            out.add(packet);
        } finally {
            packet.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Server.getServer().getLogger().error(PocketLogging.Server.NETWORK, "An error occurred whilst decoding a packet", new Object[]{
            cause
        });
    }
}
