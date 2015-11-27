package com.pocketserver.api.event.server;

import com.pocketserver.api.event.Event;

public class ServerPingEvent extends Event {
    private String motd;

    public ServerPingEvent(String motd) {
        this.motd = motd;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd == null ? "" : motd;
    }
}
