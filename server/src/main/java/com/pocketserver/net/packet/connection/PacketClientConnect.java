package com.pocketserver.net.packet.connection;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

/**
 * @author Connor Spencer Harries
 */
public class PacketClientConnect extends Packet {
    @Override
    public void read(ByteBuf buf) throws Exception {
        Server.getServer().getLogger().info(PocketLogging.Server.NETWORK, "A client is logging in!");
    }
}
