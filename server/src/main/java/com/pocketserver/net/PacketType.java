package com.pocketserver.net;

import com.google.common.base.Preconditions;

/**
 * @author DarkSeraphim.
 */
public interface PacketType {
    byte getId();

    boolean hasExtraIds();

    byte[] getExtraIds();

    Class<? extends Packet> getPacketClass();

    boolean isClientPacket();

    default Packet createPacket() {
        Preconditions.checkArgument(this.isClientPacket(), "Client packets require a createPacket() implementation");
        return null;
    }
}
