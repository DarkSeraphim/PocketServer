package com.pocketserver.example.command;

import com.pocketserver.api.command.Command;
import com.pocketserver.api.command.CommandExecutor;
import com.pocketserver.example.ExampleListener;

/**
 * @author PocketServer Team
 */
public class CommandChangeName extends Command {
    private final ExampleListener listener;

    public CommandChangeName(ExampleListener listener) {
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
