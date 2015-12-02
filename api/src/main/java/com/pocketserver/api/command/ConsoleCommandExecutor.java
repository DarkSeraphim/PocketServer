package com.pocketserver.api.command;

import com.pocketserver.api.Server;

/**
 * The executor for a console command.
 *
 * @author TheLightMC
 * @author Nick
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 *
 * @see CommandExecutor
 */
public class ConsoleCommandExecutor implements CommandExecutor {
    private final Server server;

    public ConsoleCommandExecutor(Server server) {
        this.server = server;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "**CONSOLE**";
    }

    /**
     * Prints a message to the server {@link org.slf4j.Logger}
     *
     * @param message string to log
     */
    @Override
    public void sendMessage(String message) {
        server.getLogger().info(message);
    }

    /**
     * @return {@code true} as console has all permissions
     */
    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOp() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public void setOp(boolean op) {

    }
}
