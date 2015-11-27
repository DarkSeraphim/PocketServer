package com.pocketserver.api.event;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.api.util.PocketLogging;

@SuppressWarnings("unchecked")
public final class EventBus {
    private final Multimap<Class<? extends Event>, EventDelegate> delegateMap;
    private final ReadWriteLock delegateLock;

    public EventBus() {
        this.delegateLock = new ReentrantReadWriteLock();
        this.delegateMap = HashMultimap.create();
    }

    public void registerListener(Plugin plugin, Listener listener) {
        Preconditions.checkNotNull(plugin, "plugin should not be null");
        Preconditions.checkNotNull(listener, "listener should not be null");
        Preconditions.checkArgument(plugin.isEnabled(), "plugin is trying to register listener when it is disabled");

        delegateLock.writeLock().lock();
        try {
            for (Method method : listener.getClass().getMethods()) {
                if (method.isAnnotationPresent(Subscribe.class)) {
                    Class[] params = method.getParameterTypes();
                    Class<?> clazz;
                    if (params.length == 1 && Event.class.isAssignableFrom(clazz = params[0])) {
                        EventDelegate delegate = new EventDelegate(listener, plugin, method);
                        delegateMap.put((Class<? extends Event>) clazz, delegate);
                        plugin.getLogger().debug(PocketLogging.Plugin.EVENT, "Registered handler {}", dumpMethod(method));
                    } else {
                        plugin.getLogger().warn(PocketLogging.Plugin.EVENT, "Event handler is incorrectly setup: {}", dumpMethod(method));
                    }
                }
            }
        } finally {
            delegateLock.writeLock().unlock();
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
        Preconditions.checkNotNull(event, "event should not be null");
        delegateLock.readLock().lock();
        try {
            for (EventDelegate delegate : delegateMap.get(event.getClass())) {
                delegate.execute(event);
            }
            event.done();
            return event;
        } finally {
            delegateLock.readLock().unlock();
        }
    }

    private void unregisterListener(Predicate<EventDelegate> predicate) {
        delegateLock.writeLock().lock();
        try {
            for(Iterator<EventDelegate> delegates = delegateMap.values().iterator(); delegates.hasNext(); ) {
                if (predicate.test(delegates.next())) {
                    delegates.remove();
                }
            }
        } finally {
            delegateLock.writeLock().unlock();
        }
    }

    private static String dumpMethod(Method method) {
        Preconditions.checkNotNull(method, "method should not be null");
        StringBuilder builder = new StringBuilder();
        builder.append(method.getDeclaringClass().getCanonicalName()).append("(");
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            builder.append(method.getParameterTypes()[i].getSimpleName());
            if (i != method.getParameterTypes().length - 1) {
                builder.append(", ");
            }
        }
        return builder.append(")").toString();
    }

    private final class EventDelegate {
        private final Listener listener;
        private final Plugin plugin;
        private final Method method;
        private final byte priority;

        public EventDelegate(Listener listener, Plugin plugin, Method method) {
            this.listener = listener;
            this.priority = 0x00;
            this.plugin = plugin;
            this.method = method;
        }

        public void execute(Event event) {
            try {
                method.invoke(listener, event);
            } catch (IllegalAccessException ex) {
                plugin.getLogger().error(PocketLogging.Plugin.EVENT, "Event handler is not accessible: {}", dumpMethod(method));
            } catch (Exception ex) {
                plugin.getLogger().error(PocketLogging.Plugin.EVENT, "Threw an exception whilst handling {}", new Object[] {
                    event.getClass().getSimpleName(),
                    ex
                });
            }
        }

        @Override
        public int hashCode() {
            return String.join(":", plugin.getName(), dumpMethod(method)).hashCode();
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("plugin", plugin.getName())
                .add("method", method.getName())
                .add("priority", priority)
                .toString();
        }
    }
}
