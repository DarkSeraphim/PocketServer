package com.pocketserver.net.packet;

import com.pocketserver.api.Server;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PipelineUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author Connor Spencer Harries
 */
public class PacketClientConnect extends Packet {
    @Override
    public void read(ByteBuf buf) throws Exception {
        Server.getServer().getLogger().info(PipelineUtils.NETWORK_MARKER, "A client is logging in!");
    }
}
