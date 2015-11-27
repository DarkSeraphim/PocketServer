package com.pocketserver.example;

import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.example.command.CommandChangeName;

/**
 * Our example plugin.
 *
 * @author PocketServer Team
 * @version 1.0-SNAPSHOT
 */
public final class ExamplePlugin extends Plugin {
    @Override
    public void onEnable() {
        ExampleListener listener = new ExampleListener();
        getServer().getEventBus().registerListener(this, listener);
        getServer().getCommandManager().registerCommand(new CommandChangeName(listener));
    }
}
