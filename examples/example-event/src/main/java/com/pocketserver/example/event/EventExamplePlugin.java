package com.pocketserver.example.event;

import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.example.event.command.CommandChangeName;
import com.pocketserver.example.event.event.ServerListener;

public final class EventExamplePlugin extends Plugin {
    @Override
    public void onEnable() {
        ServerListener listener = new ServerListener();
        getServer().getEventBus().registerListener(this, listener);
        getServer().getCommandManager().registerCommand(new CommandChangeName(listener));
    }
}
