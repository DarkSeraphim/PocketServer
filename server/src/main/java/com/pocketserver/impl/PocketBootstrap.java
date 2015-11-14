package com.pocketserver.impl;

import com.pocketserver.impl.console.ArgumentParser;
import com.pocketserver.impl.gui.ConsoleWindow;

public class PocketBootstrap {
    public static void main(String[] args) {
        PocketServer pocketServer = new PocketServer();

        ArgumentParser parser = new ArgumentParser(args);
        if (true) {//if (parser.isAllowed("gui")) {
            System.out.println("lol");
            ConsoleWindow window = new ConsoleWindow(pocketServer.getOnlinePlayers());
            window.setVisible(true);
            //JFrame frame = new JFrame("");
        }
    }
}
