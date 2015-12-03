package com.pocketserver.api.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.pocketserver.api.Server;
import com.pocketserver.api.command.Command;
import com.pocketserver.api.event.Event;
import com.pocketserver.api.event.Listener;
import com.pocketserver.api.exceptions.InvalidPluginException;
import com.pocketserver.api.util.PocketLogging;
import org.slf4j.LoggerFactory;

public class PluginManager {
    public static final FileFilter JAR_FILTER = pathname -> pathname.getName().endsWith(".jar");

    private final SetMultimap<Plugin, Listener> listenersByPlugin;
    private final List<Plugin> plugins;
    private final EventBus eventBus;
    private final Server server;

    public PluginManager(Server server) {
        this.eventBus = new EventBus(new SubscriberExceptionHandler() {
            @Override
            public void handleException(Throwable exception, SubscriberExceptionContext context) {
                if (Listener.class.isAssignableFrom(context.getSubscriber().getClass())) {
                    Listener listener = (Listener) context.getSubscriber();
                    for (Map.Entry<Plugin, Listener> entry : listenersByPlugin.entries()) {
                        if (listener == entry.getValue()) {
                            Plugin plugin = entry.getKey();
                            plugin.getLogger().error(PocketLogging.Plugin.EVENT, "An unhandled exception was thrown by {}", new Object[]{
                                printMethod(context.getSubscriberMethod()),
                                exception
                            });
                            return;
                        }
                    }
                    server.getLogger().error(PocketLogging.Plugin.EVENT, "An unhandled exception was thrown whilst handling {}", new Object[]{
                        context.getEvent().getClass().getName(),
                        exception
                    });
                }
            }
        });
        this.listenersByPlugin = Multimaps.synchronizedSetMultimap(HashMultimap.create());
        this.plugins = Lists.newArrayList();
        this.server = server;
    }

    /**
     * Loop through every file with the {@code jar} extension in the {@code "./plugins"} directory and attempt
     * to load it. Once loaded the plugin will be enabled and PocketServer will start tracking it.
     *
     * Plugins are loaded via {@link PluginManager#loadPlugin(File)} which does <b>not</b> enable them.
     * It is possible to find out which plugins failed to load by filtering out all but the {@code PLUGINS_INIT} markers.
     *
     * @since 1.0-SNAPSHOT
     * @see PluginManager#loadPlugin(File)
     */
    public void loadPlugins() {
        File pluginsDirectory = new File(server.getDirectory(), "plugins");
        for (File file : pluginsDirectory.listFiles(JAR_FILTER)) {
            try {
                Plugin plugin = loadPlugin(file);
                plugin.setEnabled(true);
                plugins.add(plugin);
            } catch (Exception ex) {
                server.getLogger().error(PocketLogging.Plugin.INIT, "Failed to initialise plugin {}", file.getName(), ex);
            }
        }
    }

    /**
     * Attempt to <b>load</b> a {@link Plugin} from a JAR file. This method does not enable a plugin.
     *
     * @param file {@link File} to load the plugin from.
     * @return a non-null {@link Plugin} instance (assuming an {@link InvalidPluginException} was not thrown)
     * @throws InvalidPluginException when an exception that stops the plugin from being loaded is thrown
     * @since 1.0-SNAPSHOT
     * @see PluginManager#loadPlugins()
     */
    public Plugin loadPlugin(File file) throws InvalidPluginException {
        Preconditions.checkArgument(file.exists(), "provided file must exist!");
        Preconditions.checkArgument(JAR_FILTER.accept(file), "provided file must be a JAR file!");

        try (JarFile jar = new JarFile(file)) {
            JarEntry entry = jar.getJarEntry("plugin.properties");
            if (entry == null) {
                throw new InvalidPluginException(file.getName());
            }
            try (InputStream stream = jar.getInputStream(entry)) {
                if (stream == null) {
                    throw new InvalidPluginException(file.getName());
                }
                Properties properties = new Properties();
                properties.load(stream);

                PluginDescriptor descriptor = new PluginDescriptor(properties);
                try {
                    URLClassLoader loader = new URLClassLoader(new URL[] {
                       file.toURI().toURL()
                    });
                    Class clazz = loader.loadClass(descriptor.getMain());
                    if(!Plugin.class.isAssignableFrom(clazz)) {
                        throw new RuntimeException("main class is not a descendant of " + Plugin.class.getCanonicalName());
                    }
                    Plugin plugin = (Plugin) clazz.newInstance();
                    plugin.init(LoggerFactory.getLogger(descriptor.getName()), server, descriptor);
                    return plugin;
                } catch  (ReflectiveOperationException ex) {
                    throw new RuntimeException("Failed to create an instance of " + descriptor.getMain(), ex);
                }
            }
        } catch (InvalidPluginException ex) {
            // Don't throw a new InvalidPluginException
            throw ex;
        } catch (Exception ex) {
            // TODO: Cleanup
            throw new InvalidPluginException(file.getName(), ex);
        }
    }

    /**
     * Public method for toggling the state of a {@link Plugin} instance.
     * @param plugin non-null {@link Plugin} instance
     * @param enabled whether the plugin should be enabled or not
     * @throws NullPointerException if the provided {@link Plugin} is null
     */
    public void setEnabled(Plugin plugin, boolean enabled) {
        Preconditions.checkNotNull(plugin, "plugin should not be null").setEnabled(enabled);
    }

    /**
     * Retrieve an immutable list of loaded plugins.
     *
     * @return immutable list of plugins
     */
    public List<Plugin> getPlugins() {
        return ImmutableList.copyOf(plugins);
    }

    /**
     * Attempt to unload a plugin from the server.
     *
     * @param plugin plugin instance to unload
     */
    public void unloadPlugin(Plugin plugin) {
        Preconditions.checkNotNull(plugin, "plugin must not be null");
        if (plugin.isEnabled()) {
            plugin.setEnabled(false);
        }

        try {
            if (plugin.getClass().getClassLoader() instanceof URLClassLoader) {
                URLClassLoader loader = (URLClassLoader) plugin.getClass().getClassLoader();
                loader.close();
            }
        } catch (Exception ex) {
            server.getLogger().error(PocketLogging.Plugin.INIT, "Failed to unload plugin!", ex);
        }
    }

    /**
     * Test whether a plugin is currently enabled or not
     *
     * @param pluginName plugin name to test
     * @return {@code true} if a plugin with that name is found and enabled
     */
    public boolean isEnabled(String pluginName) {
        Preconditions.checkNotNull(pluginName, "pluginName should not be null!");
        for (Plugin plugin : getPlugins()) {
            if (plugin.getName().equals(pluginName)) {
                return plugin.isEnabled();
            }
        }
        return false;
    }

    public boolean registerListener(Plugin plugin, Listener listener) {
        Preconditions.checkNotNull(listener, "listener should not be null");
        Preconditions.checkNotNull(plugin, "plugin should not be null");
        eventBus.register(listener);

        Object[] logParams = new Object[]{
            listener.getClass().getCanonicalName()
        };

        if (listenersByPlugin.put(plugin, listener)) {
            plugin.getLogger().debug(PocketLogging.Plugin.EVENT, "Registered {}", logParams);
            return true;
        } else {
            plugin.getLogger().warn(PocketLogging.Plugin.EVENT, "Attempted registration of {} multiple times", logParams);
            return false;
        }
    }

    public boolean unregisterListener(Listener listener) {
        try {
            eventBus.unregister(listener);
            boolean removed = false;
            for (Iterator<Listener> listeners = listenersByPlugin.values().iterator(); listeners.hasNext(); ) {
                if (listeners.next() == listener) {
                    listeners.remove();
                    removed = true;
                }
            }
            return removed;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public void unregisterListeners(Plugin plugin) {
        Preconditions.checkNotNull(plugin, "plugin should not be null!");
        plugin.getLogger().trace(PocketLogging.Plugin.EVENT, "Unregistered {} listeners", new Object[]{
            String.valueOf(listenersByPlugin.removeAll(plugin).size())
        });
    }

    public <T extends Event> void post(T event) {
        eventBus.post(event);
    }

    // TODO: Reimplement command registration
    public void registerCommand(Plugin plugin, Command command) {

    }

    public void unregisterCommand(Command command) {

    }

    public void unregisterCommands(Plugin plugin) {

    }

    private String printMethod(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getDeclaringClass().getCanonicalName());
        builder.append("#");
        builder.append(method.getName()).append("(");
        for (Iterator<Class> iterator = Iterators.forArray(method.getParameterTypes()); iterator.hasNext(); ) {
            builder.append(iterator.next().getName());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.append(")").toString();
    }
}
