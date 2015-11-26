package com.pocketserver.api.permissions;

import java.util.List;

import com.pocketserver.api.Server;
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
 * @see Server#setPermissionResolver(PermissionResolver)
 * @see Server#getPermissionResolver()
 *
 * @deprecated Implementing this interface yourself is unwise until a stable release has been
 *             published as it is likely that it will evolve rapidly until a final design
 *             decision has been made by the project collaborators.
 */
public interface PermissionResolver {
    void setPermission(Player player, String permission, boolean state);
    boolean checkPermission(Player player, String permission);
    List<String> getPermissions(Player player);

    default void close() {

    }
}
