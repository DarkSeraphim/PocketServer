package com.pocketserver.command;

import com.pocketserver.api.ChatColor;
import com.pocketserver.api.command.Command;
import com.pocketserver.api.command.CommandExecutor;

public class CommandVersion extends Command {
    private static final String VERSION = CommandVersion.class.getPackage().getImplementationVersion();

    public CommandVersion() {
        super("version", "pocket.command.version", "ver", "versions", "pocketserver");
    }

    @Override
    public void execute(CommandExecutor executor, String[] args) {
        executor.sendMessage(ChatColor.GREEN + "The server is currently running: " + VERSION);
    }
}
