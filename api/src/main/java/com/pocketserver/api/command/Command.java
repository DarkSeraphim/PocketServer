package com.pocketserver.api.command;

import com.google.common.base.Preconditions;

import java.util.Arrays;

import com.pocketserver.api.ChatColor;

/**
 * TODO: Be all fancy.
 * @deprecated implementing this class is unwise as the design is constantly being updated.
 */
public abstract class Command {
    private final String[] aliases;
    private final String permission;
    private final String name;

    protected Command(String name) {
        this(name, "");
    }

    protected Command(String name, String permission, String... aliases) {
        Preconditions.checkNotNull(name, "name should not be null");
        Preconditions.checkArgument(name.length() > 0, "name should not be empty");
        this.aliases = Arrays.stream(aliases).filter(a -> a != null && !a.isEmpty()).toArray(String[]::new);
        this.permission = permission == null ? "" : permission.replace(" ", "");
        this.name = name.toLowerCase();
    }

    /**
     * @return immutable collection of aliases that may be used to invoke the command
     */
    public final String[] getAliases() {
        return Arrays.copyOf(aliases, aliases.length);
    }

    /**
     * Method that processes execution of the command.
     *
     * @param executor the entity that executed the command
     * @param args arguments passed to the command
     */
    public abstract void execute(CommandExecutor executor, String[] args);

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
