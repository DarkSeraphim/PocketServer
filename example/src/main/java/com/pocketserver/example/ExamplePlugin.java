package com.pocketserver.example;

import com.pocketserver.plugin.Plugin;

public final class ExamplePlugin extends Plugin {
  @Override
  public void onEnable() {
    getServer().getEventBus().registerListener(this, new ExampleListener());
  }
}
