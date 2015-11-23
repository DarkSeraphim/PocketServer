package com.pocketserver.example;

import com.pocketserver.plugin.Plugin;

/**
 * Our example plugin.
 *
 * @author PocketServer Team
 * @version 1.0-SNAPSHOT
 */
public final class ExamplePlugin extends Plugin {
    @Override
    public void onEnable() {
        getServer().getEventBus().registerListener(this, new ExampleListener());
    }
}
