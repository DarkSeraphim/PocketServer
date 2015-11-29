package com.pocketserver.net.packet.connect;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.pocketserver.net.codec.Encapsulation;
import com.pocketserver.net.codec.EncapsulationStrategy;
import com.pocketserver.net.packet.AbstractEncapsulatedPacket;
import io.netty.buffer.ByteBuf;

public class PacketConnectOpenResponse extends AbstractEncapsulatedPacket {
    private static final InetSocketAddress SYSTEM_ADDRESS = new InetSocketAddress(InetAddress.getLoopbackAddress(), 19132);

    private final long serverTimestamp;
    private final long clientTimestamp;

    public PacketConnectOpenResponse(long clientTimestamp) {
        this.serverTimestamp = System.currentTimeMillis();
        this.clientTimestamp = clientTimestamp;
    }

    @Override
    public EncapsulationStrategy getEncapsulationStrategy() {
        return Encapsulation.BARE;
    }

    @Override
    public void write(ByteBuf buf) {
        writeAddress(buf, SYSTEM_ADDRESS);
        buf.writeLong(clientTimestamp);
        buf.writeLong(serverTimestamp);
    }
}
