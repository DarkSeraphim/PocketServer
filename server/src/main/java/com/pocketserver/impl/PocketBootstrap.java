package com.pocketserver.impl;

import com.google.common.collect.ImmutableList;

import java.io.IOException;

import com.pocketserver.impl.gui.ConsoleWindow;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class PocketBootstrap {
    
    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        OptionSpec<Boolean> gui = parser.acceptsAll(ImmutableList.of("g", "gui"), "display a window containing console output").withRequiredArg().ofType(Boolean.class).defaultsTo(false);
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

        if (options.has(gui) && gui.value(options)) {
            ConsoleWindow window = new ConsoleWindow(server);
        }
    }
    
}
