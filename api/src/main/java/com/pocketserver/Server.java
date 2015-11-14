package com.pocketserver;

import com.pocketserver.event.EventBus;
import com.pocketserver.player.Player;
import com.pocketserver.plugin.PluginManager;
import org.slf4j.Logger;

import java.util.List;

public interface Server {
    EventBus getEventBus();
    PluginManager getPluginManager();
    Logger getLogger();

    boolean isRunning();

    List<? extends Player> getOnlinePlayers();
}
