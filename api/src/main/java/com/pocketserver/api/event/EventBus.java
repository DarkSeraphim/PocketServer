package com.pocketserver.api.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.api.util.PocketLogging;

public final class EventBus {
    private final ConcurrentMap<Class<?>, List<EventData>> eventListeners;
    private final Lock listenerLock;

    public EventBus() {
        this.eventListeners = new ConcurrentHashMap<>();
        this.listenerLock = new ReentrantLock();
    }

    public void registerListener(Plugin plugin, Listener listener) {
        Preconditions.checkNotNull(plugin, "plugin should not be null");
        Preconditions.checkNotNull(listener, "listener should not be null");
        Preconditions.checkArgument(plugin.isEnabled(), "plugin is trying to register listener when it is disabled");

        listenerLock.lock();
        try {
            for (Method method : listener.getClass().getMethods()) {
                if (!method.isAnnotationPresent(Subscribe.class)) {
                    continue;
                }
                Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length == 0) {
                    continue;
                }
                Class<?> type = parameters[0];
                eventListeners.computeIfAbsent(type, t -> Lists.newArrayList()).add(new EventData(plugin, listener, method));
            }
        } finally {
            listenerLock.unlock();
        }
    }

    public void unregisterListener(Listener listener) {
        Preconditions.checkNotNull(listener, "plugin should not be null");
        unregisterListener(data -> data.listener == listener);
    }

    public void unregisterListener(Plugin plugin) {
        Preconditions.checkNotNull(plugin, "plugin should not be null");
        unregisterListener(data -> data.plugin == plugin);
    }

    public <T extends Event> T post(T event) {
        if (event == null) {
            return null;
        }

        eventListeners.entrySet().stream().filter(entry -> entry.getKey().isInstance(event)).forEach(entry -> {
            entry.getValue().forEach(data -> data.invoke(event));
        });
        return event;
    }

    private void unregisterListener(Predicate<EventData> predicate) {
        listenerLock.lock();
        try {
            for (List<EventData> containers : eventListeners.values()) {
                for (Iterator<EventData> dataIterator = containers.iterator(); dataIterator.hasNext(); ) {
                    if (predicate.test(dataIterator.next())) {
                        dataIterator.remove();
                    }
                }
            }
        } finally {
            listenerLock.unlock();
        }
    }

    private class EventData {
        private final Listener listener;
        private final Plugin plugin;
        private final Method method;

        public EventData(Plugin plugin, Listener listener, Method method) {
            this.listener = listener;
            this.plugin = plugin;
            this.method = method;
        }

        public void invoke(Event event) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            try {
                method.invoke(listener, event);
            } catch (Exception ex) {
                plugin.getLogger().error(PocketLogging.Plugin.EVENT, "Failed to handle event {}", new Object[] {
                    event.getClass().getCanonicalName(),
                    ex
                });
            }
        }
    }
}
