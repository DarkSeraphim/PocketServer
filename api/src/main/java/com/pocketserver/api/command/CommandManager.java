package com.pocketserver.api.command;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    private static final String NO_PERMISSION = "No permission.";
    private static final String NO_COMMAND = "That is not a valid command.";
    private final Map<String, Command> commands = new ConcurrentHashMap<>();

    public void registerCommand(Command command) {
        Preconditions.checkNotNull(command, "Command cannot be null");

        this.commands.put(command.getCommandName(), command);
        for (String s : command.getAliases()) {
            this.commands.put(s, command);
        }
    }

    public void unregisterCommand(Command command) {
        Preconditions.checkNotNull(command, "Command cannot be null");

        this.commands.remove(command.getCommandName());
        for (String s : command.getAliases()) {
            this.commands.remove(s);
        }
    }

    public void executeCommand(CommandExecutor executor, String commandName) {
        Preconditions.checkNotNull(executor, "CommandExecutor cannot be null");
        Preconditions.checkNotNull(commandName, "Command string cannot be null");

        String[] arguments = commandName.split(" ");
        String label = arguments[0];
        Command command = getCommand(label);
        if (command == null) {
            executor.sendMessage(NO_COMMAND);
            return;
        }
        if (!command.getPermission().test(executor)) {
            executor.sendMessage(NO_PERMISSION);
        }
        command.executeCommand(executor, label, Arrays.copyOfRange(arguments,1,arguments.length));
    }

    private Command getCommand(String commandName) {
        return commands.getOrDefault(commandName, null);
    }

    public CommandExecutor getConsole() {
        return new ConsoleCommandExecutor();
    }
}
