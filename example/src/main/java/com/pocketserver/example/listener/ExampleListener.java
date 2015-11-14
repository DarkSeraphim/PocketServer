package com.pocketserver.example.listener;

import com.pocketserver.event.Listener;
import com.pocketserver.event.player.PlayerChatEvent;

public class ExampleListener {
    @Listener
    public void onPlayerChat(PlayerChatEvent event) {
        event.setCancelled(true);
        event.getPlayer().sendMessage("No talking!");
    }
}
