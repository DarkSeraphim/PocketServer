package com.pocketserver.event;

import com.pocketserver.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class EventBus {
    private final Map<Class<?>, List<EventData>> eventListeners;
    private final ExecutorService service;

    public EventBus(ExecutorService executorService) {
        this.eventListeners = new ConcurrentHashMap<>();
        this.service = executorService;
    }

    public void registerListener(Plugin plugin, Object listener) {
        if (listener == null)
            return;
        for (Method method : listener.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Listener.class)) {
                continue;
            }
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 0) {
                continue;
            }
            Class<?> type = parameters[0];
            List<EventData> dataList = eventListeners.containsKey(type) ? eventListeners.get(type) : new ArrayList<>();
            EventData data = new EventData(plugin, listener, method);
            dataList.add(data);
            eventListeners.put(type, dataList);
        }
    }

    public void unregisterListener(Object listener) {
        for (List<EventData> eventDatas : eventListeners.values()) {
            Iterator<EventData> iterator = eventDatas.iterator();
            while (iterator.hasNext()) {
                EventData next = iterator.next();
                if (next.object == listener) {
                    iterator.remove();
                }
            }
        }
    }

    public void unregisterListener(Plugin plugin) {
        for (List<EventData> eventDatas : eventListeners.values()) {
            Iterator<EventData> iterator = eventDatas.iterator();
            while (iterator.hasNext()) {
                EventData next = iterator.next();
                if (next.plugin == plugin) {
                    iterator.remove();
                }
            }
        }
    }

    public <T extends Event> Future<T> post(T event) {
        if (event == null)
            return null;
        return service.submit(() -> {
            for (Entry<Class<?>, List<EventData>> entry : eventListeners.entrySet()) {
                if (!entry.getKey().isInstance(event)) {
                    continue;
                }
                for (EventData eventData : entry.getValue()) {
                    eventData.invoke(event);
                }
            }
            return event;
        });
    }

    private class EventData {

        private final Plugin plugin;
        private final Object object;
        private final Method method;

        public EventData(Plugin plugin, Object object, Method method) {
            this.plugin = plugin;
            this.object = object;
            this.method = method;
        }

        public void invoke(Event event) {
            if (!method.isAccessible())
                method.setAccessible(true);
            try {
                method.invoke(object, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
