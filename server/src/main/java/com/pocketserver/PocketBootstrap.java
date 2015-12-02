package com.pocketserver;

import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.Scanner;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.slf4j.impl.SimpleLogger;

public class PocketBootstrap {
    public static void main(String[] args) {
        System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "[HH:mm:ss]");
        System.setProperty(SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "true");
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "debug");
        System.setProperty(SimpleLogger.LEVEL_IN_BRACKETS_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
        System.setProperty(SimpleLogger.SHOW_LOG_NAME_KEY, "true");

        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        OptionSpec<Boolean> gui = parser.acceptsAll(ImmutableList.of("g", "gui"), "display console output in a window").withRequiredArg().ofType(Boolean.class).defaultsTo(false);
        parser.acceptsAll(ImmutableList.of("v", "version"), "print the application version");
        parser.acceptsAll(ImmutableList.of("h", "help"), "print command line help");

        OptionSet options = parser.parse(args);
        if (options.has("help")) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (options.has("version")) {
            // TODO: Add git hash, possibly more information?

            String version = PocketBootstrap.class.getPackage().getImplementationVersion();
            String title = PocketBootstrap.class.getPackage().getImplementationTitle();

            if (title == null || title.isEmpty()) {
                title = "PocketServer-undefined";
            }

            if (version == null || version.isEmpty()) {
                version = "undefined";
            }

            System.out.printf("%s implementing API version %s", title, version);
            return;
        }


        PocketServer server = new PocketServer();

        Scanner reader = new Scanner(System.in);
        while (server.running) {
            String line = reader.nextLine();
            if (!line.isEmpty()) {
                server.getCommandManager().dispatch(server.getCommandManager().getConsole(), line);
            }
        }
    }
    
}
