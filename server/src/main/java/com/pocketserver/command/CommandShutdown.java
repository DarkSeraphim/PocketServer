package com.pocketserver.command;

import com.pocketserver.api.Server;
import com.pocketserver.api.command.Command;
import com.pocketserver.api.command.CommandExecutor;

public final class CommandShutdown extends Command {
    private final Server server;

    public CommandShutdown(Server server) {
        super("stop");
        this.server = server;
    }

    @Override
    public void executeCommand(CommandExecutor executor, String used, String[] args) {
        if (executor.isOp()) {
            server.shutdown();
        } else {
            executor.sendMessage("You are not an operator.");
        }
    }

    @Override
    public void help(CommandExecutor executor) {

    }
}
