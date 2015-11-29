package com.pocketserver.net;

import java.security.SecureRandom;
import java.util.regex.Pattern;

public interface Protocol {
    Pattern DISALLOWED_CHARS = Pattern.compile("[^" + Pattern.quote(" \u00A7!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~") + "]");
    String TEMP_IDENTIFIER = "MCPE;Test server;35;0.13.0; 0;20;20";
    long SERVER_ID = new SecureRandom().nextLong();
    long MAGIC_1 = 0x00ffff00fefefefeL;
    long MAGIC_2 = 0xfdfdfdfd12345678L;
    int RAKNET_VERSION = 7;
}
