package com.pocketserver.net.packet.connect;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketConnectOpenNewConnection extends Packet {
    @Override
    public void read(ByteBuf buf) throws Exception {
        Server.getServer().getLogger().info(PocketLogging.Server.NETWORK, "A client is logging in!");
    }
}