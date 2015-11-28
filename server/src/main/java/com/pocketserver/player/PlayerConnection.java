package com.pocketserver.player;

import java.net.InetSocketAddress;

import com.pocketserver.net.Packet;

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
