package com.pocketserver.api.command;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles all of the commands. Will process the commands and
 * then send them to the proper command executors.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class CommandManager {
    private static final String NO_COMMAND = "That is not a valid command.";
    private final Map<String, Command> commands = new ConcurrentHashMap<>();

    /**
     * Registers a command into the registery. This will allow the command to be
     * called and therefore executed.
     *
     * @param command the command to add into the registery.
     */
    public void registerCommand(Command command) {
        Preconditions.checkNotNull(command, "Command cannot be null");

        this.commands.put(command.getName(), command);
        for (String s : command.getAliases()) {
            this.commands.put(s, command);
        }
    }

    /**
     * Removes a command from the registery so it can't be executed anymore. The
     * command will now return a no command found message when called.
     *
     * @param command the command to remove from the regisery.
     */
    public void unregisterCommand(Command command) {
        Preconditions.checkNotNull(command, "Command cannot be null");

        this.commands.remove(command.getName());
        command.getAliases().forEach(this.commands::remove);
    }

    /**
     * Will attempt to find and therefore execute the command. Prior to executing it will
     * check if the command exists and if not return a no command found message. If the command is found
     * it will then call an {@link com.pocketserver.api.event.player.PlayerCommandPreProccessEvent} if the command
     * executor is an instance of a {@link com.pocketserver.api.player.Player}. If the command is cancelled then
     * nothing will be executed.
     *
     * @param executor the {@link CommandExecutor} that executed the command.
     * @param commandName the label of the command to be executed.
     */
    public void dispatch(CommandExecutor executor, String commandName) {
        Preconditions.checkNotNull(executor, "CommandExecutor cannot be null");
        Preconditions.checkNotNull(commandName, "Command string cannot be null");

        String[] arguments = commandName.split(" ");
        String label = arguments[0];
        Command command = getCommand(label);
        if (command == null) {
            executor.sendMessage(NO_COMMAND);
            return;
        }

        if(command.testPermission(executor)) {
            command.execute(executor, label, Arrays.copyOfRange(arguments, 1, arguments.length));
        }
    }

    /**
     * Retrieves a command by the name/alias and returns it.
     *
     * @param commandName name or alias of the command to retrieve.
     * @return the command if it's in the registery or returns null if not.
     */
    private Command getCommand(String commandName) {
        return commands.getOrDefault(commandName, null);
    }

    /**
     * Gives an instance of an {@link ConsoleCommandExecutor} for plugin usage.
     *
     * @return a new instance of an {@link ConsoleCommandExecutor}.
     *
     * @see ConsoleCommandExecutor
     */
    public CommandExecutor getConsole() {
        return new ConsoleCommandExecutor();
    }
}
