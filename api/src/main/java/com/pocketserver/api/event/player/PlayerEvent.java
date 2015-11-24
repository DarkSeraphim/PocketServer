package com.pocketserver.api.event.player;

import com.pocketserver.api.event.entity.EntityEvent;
import com.pocketserver.api.player.Player;

public class PlayerEvent extends EntityEvent {

    public PlayerEvent(Player player) {
        super(player);
    }

    public Player getPlayer() {
        return (Player) getEntity();
    }
}
