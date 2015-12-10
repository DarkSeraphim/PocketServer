package com.pocketserver.net.packet.raknet;

import com.pocketserver.net.PacketRegistry;

public class PacketRaknetNack extends PacketRaknetAck {
    public PacketRaknetNack() {
        super(PacketRegistry.PacketType.NACK);
    }
}
