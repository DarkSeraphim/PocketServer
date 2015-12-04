package com.pocketserver.api.event.player;

import com.pocketserver.api.ChatColor;
import com.pocketserver.api.event.AsyncEvent;
import com.pocketserver.api.event.Cancellable;
import com.pocketserver.api.util.Callback;

public class PlayerLoginEvent extends AsyncEvent<PlayerLoginEvent> implements Cancellable {
    private String kickMessage;
    private boolean cancelled;

    public PlayerLoginEvent(Callback<PlayerLoginEvent> callback) {
        super(callback);
        this.kickMessage = ChatColor.RED + "You are not allowed to join this server!";
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getKickMessage() {
        return kickMessage;
    }

    public void setKickMessage(String kickMessage) {
        if ((kickMessage == null || kickMessage.length() < 1) && isCancelled()) {
            setCancelled(false);
            return;
        }
        this.kickMessage = kickMessage;
    }
}
