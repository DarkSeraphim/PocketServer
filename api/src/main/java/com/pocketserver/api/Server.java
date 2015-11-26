package com.pocketserver.api;

import com.google.common.base.Preconditions;

import java.io.File;
import java.util.List;

import com.pocketserver.api.command.CommandManager;
import com.pocketserver.api.event.EventBus;
import com.pocketserver.api.permissions.PermissionResolver;
import com.pocketserver.api.player.Player;
import com.pocketserver.api.plugin.PluginManager;
import org.slf4j.Logger;

public abstract class Server {
    
    private static Server server;
    
    public static Server getServer() {
        return server;
    }
    
    public static void setServer(Server server) {
        Preconditions.checkState(Server.server == null, "cannot redefine Server singleton!");
        Server.server = server;
    }

    public abstract void shutdown();
    
    public abstract EventBus getEventBus();
    
    public abstract PluginManager getPluginManager();
    
    public abstract Logger getLogger();

    public abstract boolean isRunning();

    public abstract List<? extends Player> getOnlinePlayers();

    public abstract CommandManager getCommandManager();

    public abstract void setPermissionResolver(PermissionResolver permissionResolver);

    public abstract PermissionResolver getPermissionResolver();

    public abstract File getDirectory();
}
