package com.pocketserver.impl.exception;

import com.pocketserver.impl.net.Packet;

public class InvalidPacketException extends RuntimeException {
    private final Class<? extends Packet> packet;

    public InvalidPacketException(String message,Class<? extends Packet> packet) {
        super(message);
        this.packet = packet;
    }

    public InvalidPacketException(Class<? extends Packet> packet) {
        this("Packet " + packet.getName() + " wasn't received correctly.",packet);
    }

    public Class<? extends Packet> getPacket() {
        return packet;
    }
}
