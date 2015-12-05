package com.pocketserver.api.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.api.util.Callback;

@SuppressWarnings("unchecked")
public class AsyncEvent<T extends Event> extends Event {
    private final Callback<T> callback;
    private final Set<Plugin> intents;
    private final AtomicBoolean fired;
    private final AtomicInteger latch;

    public AsyncEvent(Callback<T> callback) {
        this.intents = Sets.newSetFromMap(Maps.newConcurrentMap());
        this.fired = new AtomicBoolean();
        this.latch = new AtomicInteger();
        this.callback = (val, err) -> {
            try {
                callback.done(val, err);
            } finally {
                if (leak != null) {
                    leak.close();
                }
            }
        };
    }

    public final void registerIntent(Plugin plugin) {
        Preconditions.checkState(!fired.get(), "event has already been fired");
        if (intents.add(plugin)) {
            latch.incrementAndGet();
        }
    }

    public final void completeIntent(Plugin plugin) {
        Preconditions.checkArgument(!intents.contains(plugin), "plugin has already registered intents");
        intents.remove(plugin);
        if (fired.get()) {
            if (latch.decrementAndGet() == 0) {
                callback.done((T) this, null);
            }
        } else {
            latch.decrementAndGet();
        }
    }

    @Override
    void done() {
        if (latch.get() == 0) {
            callback.done((T) this, null);
        }
        fired.set(true);
    }
}
