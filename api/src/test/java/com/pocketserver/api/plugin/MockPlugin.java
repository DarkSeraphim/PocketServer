package com.pocketserver.api.plugin;

import com.pocketserver.api.Server;
import com.pocketserver.api.TestLogger;
import org.slf4j.LoggerFactory;

public class MockPlugin extends Plugin {
    public MockPlugin(Server server) {
        TestLogger.init();
        PluginDescriptor descriptor = new PluginDescriptor("MockPlugin", "1.0", "PocketServer Team", MockPlugin.class.getCanonicalName());
        init(LoggerFactory.getLogger(MockPlugin.class), server, descriptor);
    }
}
