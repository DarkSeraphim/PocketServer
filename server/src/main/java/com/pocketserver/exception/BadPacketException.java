package com.pocketserver.exception;

import com.pocketserver.net.Packet;

public class BadPacketException extends RuntimeException {
    private final Class<? extends Packet> packet;

    public BadPacketException(String message, Class<? extends Packet> packet) {
        super(message);
        this.packet = packet;
    }

    public BadPacketException(String message) {
        this(message, null);
    }

    public Class<? extends Packet> getPacket() {
        return packet;
    }
}
