package com.pocketserver.net.packet.notify;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketNotifyBanned extends Packet {
    @Override
    public void read(ByteBuf buf) throws Exception {
        Server.getServer().getLogger().trace(PocketLogging.Server.NETWORK, "Connection has been banned");
    }

    @Override
    public void write(ByteBuf buf) throws Exception {

    }
}
