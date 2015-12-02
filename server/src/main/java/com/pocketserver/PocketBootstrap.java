package com.pocketserver;

import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.Scanner;

import ch.qos.logback.classic.LoggerContext;
import com.pocketserver.api.ChatColor;
import com.pocketserver.util.LoggingColourFilter;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PocketBootstrap {
    public static void main(String[] args) throws Exception {
        AnsiConsole.systemInstall();

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.addTurboFilter(new LoggingColourFilter());

        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.acceptsAll(ImmutableList.of("c", "colours"), "print colours to the console");
        parser.acceptsAll(ImmutableList.of("v", "version"), "print the application version");
        parser.acceptsAll(ImmutableList.of("h", "help"), "print command line help");

        OptionSet options = parser.parse(args);
        if (options.has("c")) {
            Logger logger = context.getLogger("PocketServer");
            for (ChatColor color : ChatColor.values()) {
                logger.info(color + "{}", new Object[]{
                    color.name()
                });
            }
            return;
        }

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
