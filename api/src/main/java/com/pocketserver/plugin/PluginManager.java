package com.pocketserver.plugin;

import com.pocketserver.event.EventBus;
import com.pocketserver.exceptions.InvalidPluginException;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PluginManager {
    private final Deque<Plugin> queue = new ArrayDeque<>();
    private final List<Plugin> plugins = new ArrayList<>();
    private final EventBus bus;

    public PluginManager(EventBus bus) {
        this.bus = bus;
    }

    public void loadPlugins() {
        File pluginDirectory = new File("plugins");
        if (!pluginDirectory.exists()) {
            pluginDirectory.mkdir();
            return;
        }

        File[] files = pluginDirectory.listFiles((dir, name) -> {
            return name.endsWith(".jar");
        });
        for (File file : files) {
            Plugin plugin = loadFile(file);
            if (plugin == null) {
                throw new InvalidPluginException(file.getName());
            }
            this.registerPlugin(plugin); //TODO: Add to last in queue if no dependencies.
        }
    }

    public void registerPlugin(Plugin plugin) {
        this.plugins.add(plugin);
        plugin.onEnable();
    }

    public Plugin loadFile(File file) {
        return null;
    }

    public void unloadPlugin(Plugin plugin) {
        this.plugins.remove(plugin);
        this.getEventBus().unregisterListener(plugin);
        plugin.onDisable();
    }

    public EventBus getEventBus() {
        return bus;
    }
}
