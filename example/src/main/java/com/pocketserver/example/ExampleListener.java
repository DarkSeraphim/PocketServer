package com.pocketserver.example;

import com.pocketserver.event.Listener;
import com.pocketserver.event.player.PlayerChatEvent;

/**
 * @author PocketServer Team
 * @version 1.0-SNAPSHOT
 */
public final class ExampleListener {
    @Listener
    public void onChat(PlayerChatEvent event) {
        if (event.getMessage().equals("hello")) {
          event.getPlayer().sendMessage("Hello world, beautiful day!");
        }
    }
}
