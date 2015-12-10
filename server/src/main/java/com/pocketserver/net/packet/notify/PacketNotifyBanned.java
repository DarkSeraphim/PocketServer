package com.pocketserver.net.packet.notify;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;

public class PacketNotifyBanned extends Packet {

    public PacketNotifyBanned() {
        super(PacketRegistry.PacketType.CONNECTION_BANNED);
    }

    @Override
    public void read(ByteBuf buf) throws Exception {
        Server.getServer().getLogger().trace(PocketLogging.Server.NETWORK, "Connection has been banned");
    }

    @Override
    public void write(ByteBuf buf) throws Exception {

    }
}
