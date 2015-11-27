package com.pocketserver.net.packet;

import com.pocketserver.api.Server;
import com.pocketserver.api.event.server.ServerPingEvent;
import com.pocketserver.net.Packet;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;

public class PacketPingUnconnectedPong extends Packet {
    private final long pingTimestamp;

    public PacketPingUnconnectedPong(long pingTimestamp) {
        this.pingTimestamp = pingTimestamp;
    }

    @Override
    public void write(ByteBuf buf) {
        ServerPingEvent event = new ServerPingEvent(Protocol.TEMP_IDENTIFIER);
        Server.getServer().getEventBus().post(event);

        buf.writeLong(pingTimestamp);
        buf.writeLong(Protocol.SERVER_ID);
        writeMagic(buf);
        writeString(buf, event.getMotd());
    }
}
