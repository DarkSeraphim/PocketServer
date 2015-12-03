package com.pocketserver.api.plugin;

import com.google.common.eventbus.Subscribe;

import com.pocketserver.api.MockServer;
import com.pocketserver.api.event.Event;
import com.pocketserver.api.event.Listener;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;


public class PluginManagerTest {
    private final MockServer server = new MockServer();

    @Test
    public void testRegisterListener() throws Exception {
        PluginManager pluginManager = new PluginManager(server);
        Plugin plugin = new MockPlugin(server);
        TestEvent event = new TestEvent();

        Listener listener = new Listener() {
            @Subscribe
            public void onTest(TestEvent event) {
                plugin.getLogger().info("Received TestEvent");
                event.fired = true;
            }
        };

        Assert.assertTrue("listener should be subscribed to events", pluginManager.registerListener(plugin, listener));
        pluginManager.post(event);
        Assert.assertEquals("event should have been fired", true, event.fired);
    }

    @Test
    public void testUnregisterListener() throws Exception {
        PluginManager pluginManager = new PluginManager(server);
        Plugin plugin = new MockPlugin(server);
        Assume.assumeTrue("listener should be subscribed to events", pluginManager.registerListener(plugin, Listener.EMPTY_LISTENER));
        Assert.assertTrue("listener should be unsubscribed from events", pluginManager.unregisterListener(Listener.EMPTY_LISTENER));
    }

    private class TestEvent extends Event {
        private boolean fired = false;
    }
}