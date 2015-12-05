package com.pocketserver.net;

import com.google.common.base.MoreObjects;

public class PacketHeader {
    private final boolean pair;
    private final boolean ack;
    private final boolean nak;

    public PacketHeader(byte header) {
        this.pair = ((header >> 4) & 1) == 1;
        this.ack = ((header >> 6) & 1) == 1;
        this.nak = ((header >> 5) & 1) == 1;
    }

    public boolean isPair() {
        return pair;
    }

    public boolean isAck() {
        return ack;
    }

    public boolean isNak() {
        return nak;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(PacketHeader.class)
            .add("pair", pair)
            .add("ack", ack)
            .add("nak", nak)
            .toString();
    }
}
