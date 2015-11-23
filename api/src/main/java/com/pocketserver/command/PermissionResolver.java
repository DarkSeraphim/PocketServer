package com.pocketserver.command;

import com.pocketserver.Server;
import com.pocketserver.player.Player;

/**
 * Provide a painless way to implement permission checking.
 * The {@link PermissionResolver#checkPermission(Player, String)} method will be called by the
 * server when {@link Player#hasPermission(String)} is invoked.
 *
 * @author Connor Spencer Harries
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 *
 * @see Player#hasPermission(String)
 * @see Server#setPermissionResolver(PermissionResolver)
 * @see Server#getPermissionResolver()
 *
 * @deprecated Implementing this interface yourself is unwise until a stable release has been
 *             published as it is likely that it will evolve rapidly until a final design
 *             decision has been made by the project collaborators.
 */
public interface PermissionResolver {
    boolean checkPermission(Player player, String permission);
}
