package com.pocketserver.impl.console;

import java.util.Scanner;
import java.util.regex.Pattern;

import com.pocketserver.Server;

public class ConsoleThread extends Thread {

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter(Pattern.compile("[\\r\\n]"));
        Server server = Server.getServer();
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
