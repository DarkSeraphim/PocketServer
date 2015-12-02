package com.pocketserver.api.permissions;

import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;

import com.pocketserver.api.player.Player;

/**
 * @author Connor Spencer Harries
 */
public class PocketPermissionResolver implements PermissionResolver {
    private static final boolean OPS_ALL_PERMISSIONS = Boolean.getBoolean("pocket.ops-have-all");

    private final Set<String> operatorPermissions;
    private final Set<String> defaultPermissions;

    public PocketPermissionResolver() {
        this.operatorPermissions = ImmutableSet.copyOf(new HashSet<String>() {{
            add("pocket.command.plugins");
            add("pocket.command.whois");
            add("pocket.command.kick");
            add("pocket.command.stop");
            add("pocket.command.ban");
        }});

        this.defaultPermissions = ImmutableSet.copyOf(new HashSet<String>() {{
            add("pocket.command.version");
            add("pocket.command.help");
        }});
    }

    @Override
    public Result checkPermission(Player player, String permission) {
        if ((OPS_ALL_PERMISSIONS && player.isOp()) || (player.isOp() && operatorPermissions.contains(permission)) || defaultPermissions.contains(permission)) {
            return Result.ALLOW;
        }
        return Result.UNSET;
    }
}
