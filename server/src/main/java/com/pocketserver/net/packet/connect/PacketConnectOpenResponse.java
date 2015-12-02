package com.pocketserver.net.packet.connect;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.pocketserver.net.Packet;
import com.pocketserver.net.packet.Encapsulated;
import io.netty.buffer.ByteBuf;

public class PacketConnectOpenResponse extends Packet implements Encapsulated {
    private static final InetSocketAddress SYSTEM_ADDRESS = new InetSocketAddress(InetAddress.getLoopbackAddress(), 19132);

    private final long serverTimestamp;
    private final long clientTimestamp;
    private InetSocketAddress address;

    public PacketConnectOpenResponse(long clientTimestamp, InetSocketAddress address) {
        this.address = address;
        this.serverTimestamp = System.currentTimeMillis();
        this.clientTimestamp = clientTimestamp;
    }

    @Override
    public void write(ByteBuf buf) {
        writeAddress(buf, address);
        buf.writeShort(0);
        writeAddress(buf, SYSTEM_ADDRESS);
        for (int i = 0; i < 9; i++) {
            writeAddress(buf, new InetSocketAddress("0.0.0.0", 19132));
        }
        buf.writeLong(clientTimestamp);
        buf.writeLong(serverTimestamp);
    }
}
