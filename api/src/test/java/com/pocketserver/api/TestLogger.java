package com.pocketserver.api;

import org.slf4j.impl.SimpleLogger;

public class TestLogger {
    static {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "debug");
        System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "false");
        System.setProperty(SimpleLogger.LEVEL_IN_BRACKETS_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "false");
    }

    public static void init() {
        /*
         * Do nothing. We just want the static block to be executed.
         */
    }
}
