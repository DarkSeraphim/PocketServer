package com.pocketserver.api;

import java.io.File;
import java.util.List;

import com.pocketserver.api.command.PermissionResolver;
import org.slf4j.Logger;

import com.pocketserver.api.command.CommandManager;
import com.pocketserver.api.event.EventBus;
import com.pocketserver.api.player.Player;
import com.pocketserver.api.plugin.PluginManager;

public abstract class Server {
    
    private static Server server;
    
    public static Server getServer() {
        return server;
    }
    
    public static void setServer(Server server) {
        if (Server.server == null)
            Server.server = server;
    }
    
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
