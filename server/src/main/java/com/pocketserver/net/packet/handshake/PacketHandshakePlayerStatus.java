package com.pocketserver.net.packet.handshake;

import com.pocketserver.api.Server;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import io.netty.buffer.ByteBuf;

public class PacketHandshakePlayerStatus extends Packet {

    public PacketHandshakePlayerStatus() {
        super(PacketRegistry.DefaultPacketType.PLAYER_STATUS);
    }

    @Override
    public void read(ByteBuf buf) throws Exception {
        int status = buf.readInt();
        switch (status) {
            case 0:
                Server.getServer().getLogger().debug("Everything is working!");
                break;
            case 1:
                Server.getServer().getLogger().debug("Client is disconnecting - server outdated");
                break;
            case 2:
                Server.getServer().getLogger().debug("Player has spawned!");
                break;
        }
    }
}
