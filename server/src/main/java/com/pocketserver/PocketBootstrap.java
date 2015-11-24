package com.pocketserver;

import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.Scanner;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class PocketBootstrap {
    public static void main(String[] args) {
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

        // TODO: Possibly move elsewhere
        // if (options.has(gui) && gui.value(options)) {
        //     ConsoleWindow window = new ConsoleWindow(server);
        // }

        Scanner reader = new Scanner(System.in);
        while (server.isRunning()) {
            // TODO: Implement console handling
            String line = reader.nextLine();
            if (!line.isEmpty()) {
                server.getCommandManager().executeCommand(server.getCommandManager().getConsole(), line);
            }
        }
    }
    
}
