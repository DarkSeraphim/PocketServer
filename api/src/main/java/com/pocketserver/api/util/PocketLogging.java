package com.pocketserver.api.util;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Provide a convenient way to access the {@link Marker} objects we use around PocketServer.
 *
 * @author Connor Spencer Harries
 * @author Nathan Severyns
 * @author Nick Robson
 */
public interface PocketLogging {
    /**
     * Markers used for tagging server related messages.
     */
    interface Server {
        /**
         * Logging {@link Marker} used to tag messages (and stack traces) that are logged during
         * application startup.
         */
        Marker STARTUP = MarkerFactory.getMarker("STARTUP");

        /**
         * Logging {@link Marker} used to tag messages (and stack traces) that are logged during
         * application shutdown.
         */
        Marker SHUTDOWN = MarkerFactory.getMarker("SHUTDOWN");

        /**
         * Logging {@link Marker} used to tag messages (and stack traces) that are logged from
         * inside an IO thread.
         */
        Marker NETWORK = MarkerFactory.getMarker("NETWORK");
    }

    /**
     * Markers used for tagging plugin related messages.
     */
    interface Plugin {
        /**
         * Logging {@link Marker} used to tag messages that record how long it takes for the
         * {@link com.pocketserver.api.plugin.Plugin#onEnable()} and {@link
         * com.pocketserver.api.plugin.Plugin#onDisable()}}
         * methods to execute. Time is given in nanos.
         */
        Marker BENCHMARK = MarkerFactory.getMarker("BENCHMARK");

        /**
         * Logging {@link Marker} used to tag events like plugin state changes.
         */
        Marker INIT = MarkerFactory.getMarker("PLUGIN_INIT");

        /**
         * Logging {@link Marker} used to tag messages that are related to event handling.
         */
        Marker EVENT = MarkerFactory.getMarker("EVENT");
    }
}
