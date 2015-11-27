package com.pocketserver.api.command;

import com.pocketserver.api.permissions.Permissible;

/**
 * The interface for all entities that are able to execute commands. They extend
 * {@link Permissible} due to Permissible being closely related with permissions for commands
 * and other things that only humans or console would have access to.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public interface CommandExecutor extends Permissible {

    /**
     * The name of the executor.
     *
     * @return name of the executor.
     */
    String getName();

    /**
     * Sends a message to the executor. It can be a colored or a simple message so long
     * as it's able to be a string.
     *
     * @param message the message to send.
     */
    void sendMessage(String message);

}
