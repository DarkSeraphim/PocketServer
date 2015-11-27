package com.pocketserver.api.command;

import org.slf4j.LoggerFactory;

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

    /**
     * Returns a constant value due to the console not having a specific name.
     *
     * @return the name of console.
     */
    @Override
    public String getName() {
        return "**CONSOLE**";
    }

    /**
     * Logs a message to the console directly.
     *
     * @param message the message to send.
     */
    @Override
    public void sendMessage(String message) {
        LoggerFactory.getLogger("PocketServer").info(message);
    }

    /**
     * Always returns a constant value of true since console has all permissions.
     *
     * @param permission doesn't do anything.
     * @return true
     */
    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    /**
     * This method does absolutely nothing due to the console already
     * having all permissions.
     *
     * @param permission doesn't do anything.
     * @param value doesn't do anything.
     *
     * @deprecated since the method doesn't do anything.
     */
    @Deprecated
    @Override
    public void setPermission(String permission, boolean value) {
    }

    /**
     * Always returns true.
     *
     * @return a constant value of true.
     *
     * @see #hasPermission(String)
     */
    @Override
    public boolean isOp() {
        return true;
    }

    /**
     * Doesn't do anything due to the console always being op.
     *
     * @param op doesn't do anything.
     *
     * @deprecated since the method doesn't do anything.
     */
    @Deprecated
    @Override
    public void setOp(boolean op) {

    }
}
