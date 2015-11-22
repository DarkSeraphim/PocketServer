package com.pocketserver.impl.net;

import java.util.regex.Pattern;

public final class Protocol {
    private Protocol() {}

    public static final long TEMP_SERVER_ID = 0x00000000372cdc9e;
    public static final String TEMP_IDENTIFIER = "MCPE;Test server;35;0.12.1; 0;20;20";

    public static final Pattern DISALLOWED_CHARS = Pattern.compile("[^" + Pattern.quote(" !\"§#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~") + "]");

    public static final long MAGIC_1 = 0x00ffff00fefefefeL;
    public static final long MAGIC_2 = 0xfdfdfdfd12345678L;

    public static final int RAKNET_VERSION = 7;
}
