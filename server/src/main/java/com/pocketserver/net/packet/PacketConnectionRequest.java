package com.pocketserver.net.packet;

import java.util.Optional;

import com.pocketserver.PocketServer;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketConnectionRequest extends Packet {
    private long timestamp;
    private long clientId;
    private byte sec;

    @Override
    public void read(ByteBuf buf) throws Exception {
        PocketServer.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Reading from 0x09.");
        clientId = buf.readLong();
        timestamp = buf.readLong();
        sec = buf.readByte();
    }

    @Override
    public Optional<Packet> handle(ChannelHandlerContext ctx) throws Exception {
        PocketServer.getServer().getLogger().debug(PocketLogging.Server.NETWORK, "Handling an 0x09 packet.");
        return Optional.of(new PacketConnectionRequestAccepted(timestamp).setRemote(getRemote()));
    }
}
