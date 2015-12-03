package com.pocketserver.example.event.event;

import com.google.common.eventbus.Subscribe;

import com.pocketserver.api.event.Listener;
import com.pocketserver.api.event.server.ServerPingEvent;

public final class ServerListener implements Listener {
    private String serverName;

    public ServerListener() {
        this.serverName = "Example Plugin";
    }

    @Subscribe
    public void onPing(ServerPingEvent event) {
        // We'll cook up a nice object for doing this in future.s
        event.setMotd(String.format("MCPE;%s;35;0.13.0; 0;20;20", serverName));
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
