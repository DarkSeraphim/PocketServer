package com.pocketserver.api.plugin;

import com.google.common.base.Preconditions;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import org.slf4j.Logger;

public abstract class Plugin {
    private PluginDescriptor descriptor;
    private boolean initialised;
    private Logger logger;
    private Server server;
    private boolean enabled;

    public void onEnable() {

    }

    public void onDisable() {

    }

    public final Logger getLogger() {
        return this.logger;
    }

    public final Server getServer() {
        return server;
    }

    public final PluginDescriptor getPluginDescriptor() {
        return descriptor;
    }

    public final String getName() {
        return this.descriptor.getName();
    }

    public final boolean isEnabled() {
        return this.enabled;
    }

    protected final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            String what = enabled ? "enable" : "disable";
            logger.debug("Attempted to {} but the plugin is already {}d", what, what);
            return;
        }

        this.enabled = enabled;
        if (this.enabled) {
            logger.info(PocketLogging.Plugin.INIT, "Enabling {} v{} by {}", descriptor.getName(), descriptor.getVersion(), descriptor.getAuthor());
            logger.debug(PocketLogging.Plugin.INIT, "Main class: {}", descriptor.getMain().getCanonicalName());
            benchmarkMethod("onEnable", this::onEnable);
        } else {
            logger.info(PocketLogging.Plugin.INIT, "Disabling {}", descriptor.getName());
            benchmarkMethod("onDisable", this::onDisable);
            getServer().getEventBus().unregisterListener(this);
        }
    }

    private void benchmarkMethod(String methodName, Runnable action) {
        long start = System.nanoTime();
        try {
            action.run();
        } finally {
            logger.debug(PocketLogging.Plugin.BENCHMARK, "Executing {} took {} nanos", methodName, System.nanoTime() - start);
        }
    }

    final void init(Logger logger, Server server, PluginDescriptor descriptor) {
        Preconditions.checkArgument(!initialised, "plugin has already been initialised!");
        this.descriptor = descriptor;
        this.logger = logger;
        this.server = server;

        this.initialised = true;
    }
}
