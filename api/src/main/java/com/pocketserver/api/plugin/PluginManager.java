package com.pocketserver.api.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.pocketserver.api.Server;
import com.pocketserver.api.exceptions.InvalidPluginException;
import com.pocketserver.api.util.PocketLogging;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.LoggerFactory;

public class PluginManager {
    /**
     * File filter that only "accepts" JAR files.
     */
    public static final FileFilter JAR_FILTER = pathname -> pathname.getName().endsWith(".jar");

    private final List<Plugin> plugins;
    private final Server server;

    public PluginManager(Server server) {
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

                // Force explicit garbage collection to release Windows' lock on the JAR file
                if (PlatformDependent.isWindows()) {
                    System.gc();
                }
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
}
