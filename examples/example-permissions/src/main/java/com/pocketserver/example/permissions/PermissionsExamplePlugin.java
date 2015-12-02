package com.pocketserver.example.permissions;

import com.pocketserver.api.permissions.PermissionResolver;
import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.example.permissions.storage.PermissionLoader;
import com.pocketserver.example.permissions.storage.impl.FilePermissionLoader;

public class PermissionsExamplePlugin extends Plugin {
    private PermissionLoader permissionLoader;

    @Override
    public void onEnable() {
        permissionLoader = new FilePermissionLoader(this);
        getServer().getPermissionPipeline().addFirst((player, permission) -> {
            if (permissionLoader.get(player.getName()).contains(permission)) {
                return PermissionResolver.Result.ALLOW;
            }
            return PermissionResolver.Result.UNSET;
        });
    }
}
