package com.pocketserver.impl.net.util;

import io.netty.buffer.ByteBuf;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public final class PacketUtils {
    private PacketUtils() {}

    public static byte[] getTriad(int num) {
        byte b1 = (byte)((num >> 16) & 0xFF);
        byte b2 = (byte)((num >> 8) & 0xFF);
        byte b3 = (byte)(num & 0xFF);
        return new byte[]{b1,b2,b3};
    }

    public static int readTriad(ByteBuf buf) {
        ByteBuf byteBuf = buf.readBytes(new byte[3]);
        byte[] bytes = byteBuf.array();
        return (bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8) | ((bytes[2] & 0x0F) << 16);
    }
}
