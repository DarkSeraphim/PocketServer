package com.pocketserver.net.packet.notify;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;

public class PacketNotifyDisconnect extends Packet {

    public PacketNotifyDisconnect() {
        super(PacketRegistry.DefaultPacketType.NOTIFY_DISCONNECT);
    }

    @Override
    public void read(ByteBuf buf) throws Exception {
        Server.getServer().getLogger().info(PocketLogging.Server.NETWORK, "Our clients hate us :(");
    }

    @Override
    public void write(ByteBuf buf) throws Exception {
        // NOP
    }
}
