package com.pocketserver.net.packet.play;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;

public class PacketPlayStartGame extends Packet {
    // TODO: set packet type
    public PacketPlayStartGame() {
        super(PacketRegistry.PacketType.UNKNOWN);
    }
}
