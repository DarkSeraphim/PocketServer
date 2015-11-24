package com.pocketserver.exception;

import com.pocketserver.net.Packet;

public class InvalidPacketException extends RuntimeException {

    private static final long serialVersionUID = -8707220715813143314L;
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
