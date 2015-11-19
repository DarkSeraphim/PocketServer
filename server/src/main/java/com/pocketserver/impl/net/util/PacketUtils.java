package com.pocketserver.impl.net.util;

import com.google.common.base.Preconditions;
import com.pocketserver.impl.net.Protocol;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public final class PacketUtils {
    private PacketUtils() {}

    public static byte[] getTriad(int num) {
        byte b1 = (byte)((num >> 16) & 0xFF);
        byte b2 = (byte)((num >> 8) & 0xFF);
        byte b3 = (byte)(num & 0xFF);
        return new byte[]{b1,b2,b3};
    }

    public static int readTriad(ByteBuf buf) {
        ByteBuf bytes = buf.readBytes(new byte[3]);
        return (bytes.readByte() & 0xFF) | ((bytes.readByte() & 0xFF) << 8) | ((bytes.readByte() & 0x0F) << 16);
    }

    public static void writeString(ByteBuf buf, String str) {
        Preconditions.checkNotNull(str, "Cannot write a null string.");
        str = Protocol.DISALLOWED_CHARS.matcher(str).replaceAll("");
        buf.writeShort(str.length());
        buf.writeBytes(str.getBytes(Charset.defaultCharset()));
    }

    public static String readString(ByteBuf buf) {
        short len = buf.readShort();
        return buf.readBytes(len).toString(Charset.defaultCharset());
    }
}
