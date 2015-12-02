package com.pocketserver.example.event.command;

import com.pocketserver.api.command.Command;
import com.pocketserver.api.command.CommandExecutor;
import com.pocketserver.example.event.event.ServerListener;

public class CommandChangeName extends Command {
    private final ServerListener listener;

    public CommandChangeName(ServerListener listener) {
        super("change-name");
        this.listener = listener;
    }

    @Override
    public void execute(CommandExecutor executor, String used, String[] args) {
        if (args.length > 0) {
            listener.setServerName(String.join(" ", args));
        } else {
            executor.sendMessage("You must give me a name!");
        }
    }
}
