package com.pocketserver.api.command;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

import com.pocketserver.api.ChatColor;

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
    private final List<String> aliases;
    private final String permission;
    private final String name;

    protected Command(String name) {
        this(name, "");
    }

    protected Command(String name, String permission, String... aliases) {
        this.aliases = ImmutableList.copyOf(aliases);
        this.permission = permission;
        this.name = name;
    }

    /**
     * @return immutable collection of aliases that may be used to invoke the command
     */
    public final Collection<String> getAliases() {
        return aliases;
    }

    /**
     * Method that processes execution of the command.
     *
     * @param executor whoever executed the command. Able to be a {@link ConsoleCommandExecutor} or {@link com.pocketserver.api.player.Player}
     * @param used the specific label or alias that was used for the command.
     * @param args any additional arguments that were executed alongside the command label.
     */
    public abstract void execute(CommandExecutor executor, String used, String[] args);

    /**
     * @return name used to register the command.
     */
    public final String getName() {
        return name;
    }

    /**
     * @return permission node required to execute the command
     */
    public final String getPermission() {
        return this.permission;
    }

    /**
     * Convenience method for verbosely testing if a {@link CommandExecutor} has a permission or not.
     * Effectively the same as {@code testPermission(executor, getPermission())}. This method is
     * called prior to command execution so you do <b>not</b> need to run it first.
     *
     * @param executor executor to test for permission
     * @return {@code true} if thet executor has the permission node returned by {@link Command#getPermission()}
     * @see Command#testPermission(CommandExecutor, String)
     */
    protected final boolean testPermission(CommandExecutor executor) {
        return testPermission(executor, getPermission());
    }

    /**
     * Convenience method for verbosely testing if a {@link CommandExecutor} has a permission or not.
     *
     * @param executor executor to test for permission
     * @param permission permission node to test for
     * @return {@code true} if the executor has the given permission node
     */
    protected final boolean testPermission(CommandExecutor executor, String permission) {
        if (executor.hasPermission(permission)) {
            return true;
        } else {
            executor.sendMessage(ChatColor.RED + "You do not have permission to do that!");
            return false;
        }
    }
}
