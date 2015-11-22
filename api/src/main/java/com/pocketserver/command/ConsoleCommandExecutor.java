package com.pocketserver.command;

import org.slf4j.LoggerFactory;

public class ConsoleCommandExecutor implements CommandExecutor {

    @Override
    public String getName() {
        return "**CONSOLE**";
    }

    @Override
    public void sendMessage(String message) {
        LoggerFactory.getLogger("PocketServer").info(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void setPermission(String permission, boolean value) {
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean op) {

    }
}
