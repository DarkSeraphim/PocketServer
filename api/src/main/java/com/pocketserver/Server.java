package com.pocketserver;

import java.util.List;

import com.pocketserver.command.CommandManager;
import org.slf4j.Logger;

import com.pocketserver.event.EventBus;
import com.pocketserver.player.Player;
import com.pocketserver.plugin.PluginManager;

public interface Server {
    
    EventBus getEventBus();
    
    PluginManager getPluginManager();
    
    Logger getLogger();

    boolean isRunning();

    List<? extends Player> getOnlinePlayers();

    CommandManager getCommandManager();
}
