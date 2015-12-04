package com.pocketserver.api.event;

import com.google.common.base.Preconditions;

import com.pocketserver.api.plugin.PluginManager;

/**
 * Utility class for ensuring events are handled properly.
 *
 * @deprecated unless you are writing a {@link com.pocketserver.api.plugin.PluginManager}
 * implementation
 * then you probably shouldn't be touching this.
 */
public final class Events {
    private Events() {
        throw new UnsupportedOperationException("Events cannot be instantiated!");
    }

    /**
     * Utility method for triggering possible callbacks.
     */
    public static void done(Event event) {
        Preconditions.checkNotNull(event, "event");
        event.done();
    }

    public static void prepare(PluginManager pluginManager, Event event) {
        event.getLeak().record(pluginManager);
    }

    public static void record(Event event, Object hint) {
        Preconditions.checkNotNull(event, "event").getLeak().record(hint);
    }
}
