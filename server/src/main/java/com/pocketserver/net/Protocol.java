package com.pocketserver.net;

import java.security.SecureRandom;
import java.util.regex.Pattern;

public interface Protocol {
    Pattern DISALLOWED_CHARS = Pattern.compile("[^" + Pattern.quote(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~") + "]");
    byte[] OFFLINE_MESSAGE_ID = new byte[] {
        0x00, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe,
        (byte) 0xfe, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, 0x12, 0x34,
        0x56, 0x78
    };
    String TEMP_IDENTIFIER = "MCPE;Test server;35;0.12.1; 0;20;20";
    long SERVER_ID = new SecureRandom().nextLong();
    long MAGIC_1 = 0x00ffff00fefefefeL;
    long MAGIC_2 = 0xfdfdfdfd12345678L;
    int RAKNET_VERSION = 7;
}
