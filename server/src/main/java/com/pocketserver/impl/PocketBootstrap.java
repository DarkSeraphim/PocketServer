package com.pocketserver.impl;

import com.pocketserver.impl.console.ArgumentParser;

public class PocketBootstrap {
    
    public static void main(String[] args) {
        PocketServer server = new PocketServer();

        ArgumentParser parser = new ArgumentParser(args);
        //if (true) {//if (parser.isAllowed("gui")) {
        //    ConsoleWindow window = new ConsoleWindow(pocketServer.getOnlinePlayers());
        //    window.setVisible(true);
        //    //JFrame frame = new JFrame("");
        //}
    }
    
}
