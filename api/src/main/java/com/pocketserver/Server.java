package com.pocketserver;

import java.io.File;
import java.util.List;

import com.pocketserver.command.PermissionResolver;
import org.slf4j.Logger;

import com.pocketserver.command.CommandManager;
import com.pocketserver.event.EventBus;
import com.pocketserver.player.Player;
import com.pocketserver.plugin.PluginManager;

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
