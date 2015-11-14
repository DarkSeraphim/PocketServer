package com.pocketserver.plugin;

import com.google.common.base.Preconditions;
import com.pocketserver.Server;

import java.util.logging.Logger;

public abstract class Plugin {

    private boolean initialized;
    private Logger logger;
    private Server server;
    private PluginInfo info;

    public void onEnable() {

    }

    public void onDisable() {

    }

    final void load(Logger logger, Server server,PluginInfo info) {
        Preconditions.checkArgument(!initialized);

        this.logger = logger;
        this.server = server;
        this.initialized = true;
        this.info = info;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Server getServer() {
        return server;
    }

    public PluginInfo getInfo() {
        return info;
    }

    public String getName() {
        return this.info.value();
    }
}
