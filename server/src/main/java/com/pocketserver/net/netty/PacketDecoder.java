package com.pocketserver.net.netty;

import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

public class PacketDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        byte id = msg.content().readByte();
        Server.getServer().getLogger().debug(PipelineUtils.NETWORK_MARKER, "Received 0x{} from {}", String.format("%02x", id), msg.sender());
        Packet packet = PacketRegistry.construct(id);
        packet.setRemote(msg.sender());
        packet.read(msg.content());
        out.add(packet);
    }
}
