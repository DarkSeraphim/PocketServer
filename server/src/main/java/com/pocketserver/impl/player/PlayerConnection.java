package com.pocketserver.impl.player;

import com.pocketserver.impl.net.Packet;

import java.net.InetSocketAddress;

public class PlayerConnection {
    private final InetSocketAddress address;

    public PlayerConnection(InetSocketAddress address) {
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void sendPacket(Packet packet) {
        //packet.sendPacket(ctx,address);
    }
}
