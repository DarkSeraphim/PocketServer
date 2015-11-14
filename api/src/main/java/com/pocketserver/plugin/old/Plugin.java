package com.pocketserver.plugin.old;

import java.io.File;

import org.slf4j.Logger;

import com.pocketserver.Server;
import com.pocketserver.event.EventBus;

public abstract class Plugin {
/*
    public static final String DEFAULT_VERSION = "1.0.0";
    public static final String DEFAULT_DESCRIPTION = "";

    File file;
    Logger logger;

    public final String getName() {
        Name name = getClass().getAnnotation(Name.class);
        if (name != null)
            return name.value();
        PluginData data = getClass().getAnnotation(PluginData.class);
        if (data != null)
            return data.name();
        return getClass().getSimpleName();
    }

    public final String getVersion() {
        Version ver = getClass().getAnnotation(Version.class);
        if (ver != null)
            return ver.value();
        PluginData data = getClass().getAnnotation(PluginData.class);
        if (data != null)
            return data.version();
        return DEFAULT_VERSION;
    }

    public final String getDescription() {
        Description desc = getClass().getAnnotation(Description.class);
        if (desc != null)
            return desc.value();
        PluginData data = getClass().getAnnotation(PluginData.class);
        if (data != null)
            return data.description();
        return DEFAULT_DESCRIPTION;
    }

    public String[] getDependencies() {
        Dependency dep = getClass().getAnnotation(Dependency.class);
        if (dep != null)
            return dep.value();
        PluginData data = getClass().getAnnotation(PluginData.class);
        if (data != null)
            return data.dependency();
        return new String[0];
    }

    protected final File getFile() {
        return file;
    }

    public final Logger getLogger() {
        return logger;
    }

    public final File getDataFolder() {
        return new File("plugins/" + getName() + "/");
    }

    public final Server getServer() {
        return Server.getServer();
    }

    public final EventBus getEventBus() {
        return Server.getServer().getEventBus();
    }

    public void onLoad() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }
*/
}
