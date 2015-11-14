package com.pocketserver.example.listener;

import com.pocketserver.plugin.Plugin;
import com.pocketserver.plugin.PluginInfo;

@PluginInfo("Listener Example")
public class ExamplePlugin extends Plugin {
    @Override
    public void onEnable() {
        getServer().getEventBus().registerListener(this, new ExampleListener());
    }
}
