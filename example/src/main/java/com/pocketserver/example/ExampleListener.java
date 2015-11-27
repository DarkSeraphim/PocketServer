package com.pocketserver.example;

import com.pocketserver.api.event.Listener;
import com.pocketserver.api.event.Subscribe;
import com.pocketserver.api.event.server.ServerPingEvent;

/**
 * Our example listener.
 *
 * @author PocketServer Team
 * @version 1.0-SNAPSHOT
 */
public final class ExampleListener implements Listener {
    @Subscribe
    public void onChat(ServerPingEvent event) {
        // We'll cook up a nice object for doing this in future.
        event.setMotd("MCPE;Example plugin;35;0.13.0; 0;20;20");
    }
}
