package com.pocketserver.net.util;

import java.nio.charset.Charset;

import com.google.common.base.Preconditions;
import com.pocketserver.net.Protocol;

import io.netty.buffer.ByteBuf;

public final class PacketUtils {
    
    private PacketUtils() {}

    public static void writeString(ByteBuf buf, String str) {
        Preconditions.checkNotNull(str, "Cannot write a null string.");
        str = Protocol.DISALLOWED_CHARS.matcher(str).replaceAll("");
        buf.writeShort(str.length());
        buf.writeBytes(str.getBytes(Charset.defaultCharset()));
    }

    public static String readString(ByteBuf buf) {
        short len = buf.readShort();
        System.out.println("Len" + len);
        return buf.readBytes(len).toString(Charset.defaultCharset());
    }
}
