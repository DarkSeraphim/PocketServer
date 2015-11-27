package com.pocketserver.command;

import com.pocketserver.api.Server;
import com.pocketserver.api.command.Command;
import com.pocketserver.api.command.CommandExecutor;

public final class CommandShutdown extends Command {
    private final Server server;

    public CommandShutdown(Server server) {
        super("stop", "pocket.command.stop", "end", "shutdown", "quit");
        this.server = server;
    }

    @Override
    public void execute(CommandExecutor executor, String used, String[] args) {
        server.shutdown();
    }
}
