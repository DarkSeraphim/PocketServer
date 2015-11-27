package com.pocketserver.example;

import com.pocketserver.api.event.Listener;
import com.pocketserver.api.event.Subscribe;
import com.pocketserver.api.event.player.PlayerChatEvent;

/**
 * Our example listener.
 *
 * @author PocketServer Team
 * @version 1.0-SNAPSHOT
 */
public final class ExampleListener implements Listener {
    @Subscribe
    public void onChat(PlayerChatEvent event) {
        if (event.getMessage().equals("hello")) {
          event.getPlayer().sendMessage("Hello world, beautiful day!");
        }
    }
}
