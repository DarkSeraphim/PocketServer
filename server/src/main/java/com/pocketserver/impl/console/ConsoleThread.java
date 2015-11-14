package com.pocketserver.impl.console;

import java.util.Scanner;
import java.util.regex.Pattern;

import com.pocketserver.Server;
import com.pocketserver.impl.PocketServer;

public class ConsoleThread extends Thread {

    private final Server server;

    public ConsoleThread(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter(Pattern.compile("[\\r\\n]"));

        while (server.isRunning()) {
            String line = scanner.nextLine().trim().replaceAll("\\s+", " ");
            executeCommand(line);
        }
        scanner.close();
    }

    private void executeCommand(String line) {
        System.out.println("Received command: " + line);
    }
}
