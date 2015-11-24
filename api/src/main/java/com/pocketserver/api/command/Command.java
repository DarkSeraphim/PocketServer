package com.pocketserver.api.command;

import java.util.function.Predicate;

public abstract class Command {
    private final String commandName;
    private final String[] aliases;

    public Command(String name, String... aliases) {
        this.commandName = name;
        this.aliases = aliases;
    }

    public final String[] getAliases() {
        return aliases.clone();
    }

    public abstract void executeCommand(CommandExecutor executor, String used, String[] args);
    public abstract void help(CommandExecutor executor);

    public String getCommandName() {
        return commandName;
    }

    public Predicate<CommandExecutor> getPermission() {
        return executor -> true;
    }
}
