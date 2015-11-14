package com.pocketserver.exceptions;

public class InvalidPluginException extends RuntimeException {
    private final String pluginName;

    public InvalidPluginException(String pluginName) {
        super("The plugin, " + pluginName + ", was not a valid plugin.");
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }
}
