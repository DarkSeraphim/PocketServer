package com.pocketserver.api.permissions;

import com.pocketserver.api.player.Player;

/**
 * Provide a painless way to implement permission checking.
 * The {@link PermissionResolver#checkPermission(Player, String)} method will be called by the
 * server when {@link Player#hasPermission(String)} is invoked.
 *
 * <b>Warning</b> if your permission system is database backed then <b>you</b> should be implementing
 * result caching, PocketServer will not attempt to do this for you.
 *
 * @author Connor Spencer Harries
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 *
 * @see Player#hasPermission(String)
 *
 * @apiNote  Implementing this interface yourself is unwise until a stable release has been
 *           published as it is likely that it will evolve rapidly until a final design
 *           decision has been made by the project collaborators.
 */
public interface PermissionResolver {
    Result checkPermission(Player player, String permission);

    /**
     * Give the implementation a chance to clean up any possible database connections or flush
     * changes to disk. Always called at server shutdown.
     *
     * @throws Exception an exception thrown whilst cleaning up. Handled by the server instance.
     */
    default void close() throws Exception {

    }

    enum Result {
        /**
         * Instruct the server to move onto the next {@link PermissionResolver}
         */
        UNSET,
        /**
         * Instruct the server to {@code return false} inside the {@link Player#hasPermission(String)} method
         *
         * @deprecated unless you want to explicitly deny access to a permission then use {@link Result#UNSET}
         */
        DENY,
        /**
         * Instruct the server to {@code return true} inside the {@link Player#hasPermission(String)}} method
         */
        ALLOW
    }
}
