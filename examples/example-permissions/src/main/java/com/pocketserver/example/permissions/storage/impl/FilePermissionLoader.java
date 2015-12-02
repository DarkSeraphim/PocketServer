package com.pocketserver.example.permissions.storage.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Set;

import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.example.permissions.storage.PermissionLoader;
import org.slf4j.Logger;

public class FilePermissionLoader implements PermissionLoader {
    private final Cache<String, Set<String>> cache;
    private final File storageDirectory;
    private final Logger logger;

    public FilePermissionLoader(Plugin plugin) {
        this.storageDirectory = new File(plugin.getServer().getDirectory(), "plugins/permission-example");
        this.cache = CacheBuilder.newBuilder().maximumSize(100).build();
        this.logger = plugin.getLogger();
        if (storageDirectory.mkdirs()) {
            plugin.getLogger().info(PermissionLoader.PERMISSION_IO_MARKER, "Created \"{}\" directory", new Object[]{
                storageDirectory.toPath()
            });
        }
    }

    @Override
    public Collection<String> get(String username) {
        Preconditions.checkNotNull(username, "uniqueId should not be null!");
        Preconditions.checkArgument(username.length() > 0, "username should not be empty!");

        Set<String> permissions = cache.getIfPresent(username);
        if (permissions == null) {
            permissions = Sets.newHashSet();
            File userFile = new File(storageDirectory, username.concat(".permissions"));
            try {
                if (userFile.createNewFile()) {
                    logger.info(PermissionLoader.PERMISSION_IO_MARKER, "Created \"{}\"", new Object[]{
                        userFile.toPath()
                    });
                } else {
                    permissions.addAll(Files.readAllLines(userFile.toPath(), Charsets.UTF_8));
                }
            } catch (IOException cause) {
                logger.error(PermissionLoader.PERMISSION_IO_MARKER, "An unknown error occured during method execution", new Object[]{
                    cause
                });
            }
            cache.put(username, permissions);
        }
        return permissions;
    }
}
