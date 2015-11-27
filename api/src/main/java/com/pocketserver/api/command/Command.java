package com.pocketserver.api.command;

import java.util.function.Predicate;

/**
 * An abstract class that all in game commands should extend. These commands will be sent
 * through chat similar to the regular Minecraft game.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 *
 */
public abstract class Command {
    private final String commandName;
    private final String[] aliases;

    /**
     * Creates a new command with basic details.
     *
     * @implNote This should be called via the super method generally however it isn't required.
     *
     * @param name the primary command that will execute the command.
     * @param aliases all the other command names that will execute the command.
     */
    public Command(String name, String... aliases) {
        this.commandName = name;
        this.aliases = aliases;
    }

    /**
     * The aliases that were previously declared in the constructor are returned.
     * These aliases, while editable, will not affect the command class.
     *
     * @return the predefined array of aliases that execute the command.
     */
    public final String[] getAliases() {
        return aliases.clone();
    }

    /**
     * This method will be called whenever the command is executed.
     *
     * @param executor whoever executed the command. Able to be a {@link ConsoleCommandExecutor} or {@link com.pocketserver.api.player.Player}
     * @param used the specific label or alias that was used for the command.
     * @param args any additional arguments that were executed alongside the command label.
     */
    public abstract void executeCommand(CommandExecutor executor, String used, String[] args);

    /**
     * The primary command name that was specific in the constructor.
     *
     * @return primary command name.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Called when a {@link CommandExecutor} executes a command. This will be called
     * in order to check if the executor has permission for the command. By default everyone
     * has permission to execute the command however it can be overridden.
     *
     * @return a predicate to check if the executor has permission.
     *
     * @see CommandExecutor
     * @see Predicate
     *
     * @implNote this can be ignored completely while doing perission checks in the
     * {@link #executeCommand(CommandExecutor, String, String[])} method however it provides an easy
     * and generalized way to do permission checks.
     */
    public Predicate<CommandExecutor> getPermission() {
        return executor -> true;
    }
}
