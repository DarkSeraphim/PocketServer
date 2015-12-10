package com.pocketserver.net.packet.ping;

import com.pocketserver.api.Server;
import com.pocketserver.api.event.server.ServerPingEvent;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketPingUnconnectedPong extends Packet {
    private final long timestamp;

    public PacketPingUnconnectedPong(long timestamp) {
        super(PacketRegistry.PacketType.UNCONNECTED_PONG);
        this.timestamp = timestamp;
    }

    @Override
    public void write(ByteBuf buf) {
        ServerPingEvent event = new ServerPingEvent(Protocol.TEMP_IDENTIFIER);
        Server.getServer().getPluginManager().post(event);

        buf.writeLong(timestamp);
        buf.writeLong(Protocol.SERVER_ID);
        writeMagic(buf);
        writeString(buf, event.getMotd());
    }
}
