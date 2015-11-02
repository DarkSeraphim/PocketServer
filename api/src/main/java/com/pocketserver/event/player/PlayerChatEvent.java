package com.pocketserver.event.player;

import com.pocketserver.event.Cancellable;
import com.pocketserver.player.Player;

public class PlayerChatEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;
    private String message;

    public PlayerChatEvent(Player player, String message) {
        super(player);
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
