package com.pocketserver.exceptions;

import com.google.common.base.Preconditions;

public class InvalidPluginException extends RuntimeException {
    
    private static final long serialVersionUID = -6456000820553237600L;
    private final String pluginName;

    public InvalidPluginException(String pluginName) {
        this(pluginName, null);
    }

    public InvalidPluginException(String pluginName, Exception ex) {
        super(String.format("Failed to load \"%s\"", Preconditions.checkNotNull(pluginName, "pluginName must not be null")), ex);
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }
}
