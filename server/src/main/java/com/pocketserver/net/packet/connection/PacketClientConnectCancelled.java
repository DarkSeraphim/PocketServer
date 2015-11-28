package com.pocketserver.net.packet.connection;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

/**
 * @author Connor Spencer Harries
 */
public class PacketClientConnectCancelled extends Packet {
    @Override
    public void read(ByteBuf buf) {
        Server.getServer().getLogger().info(PocketLogging.Server.NETWORK, ":( our clients hate us");
    }
}
