package com.pocketserver.exceptions;

public class InvalidPluginException extends RuntimeException {
    
    private static final long serialVersionUID = -6456000820553237600L;
    private final String pluginName;

    public InvalidPluginException(String pluginName) {
        super("The plugin, " + pluginName + ", was not a valid plugin.");
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }
}
