package com.pocketserver.api.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import com.pocketserver.api.Server;
import com.pocketserver.api.exceptions.InvalidPluginException;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    /**
     * File filter that only "accepts" JAR files.
     */
    public static final FileFilter JAR_FILTER = pathname -> pathname.getName().endsWith(".jar");

    /**
     * Logging {@link Marker} used for filtering benchmark messages.
     */
    public static final Marker BENCHMARK_MARKER = MarkerFactory.getMarker("PLUGIN_BENCHMARK");

    /**
     * Logging {@link Marker} used for filtering messages that describe the initialisation of a plugin.
     */
    public static final Marker INIT_MARKER = MarkerFactory.getMarker("PLUGIN_INIT");

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
                server.getLogger().error(INIT_MARKER, "Failed to initialise plugin {}", file.getName(), ex);
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
                if(!Plugin.class.isAssignableFrom(descriptor.getMain())) {
                    throw new RuntimeException("main class is not a descendant of " + Plugin.class.getCanonicalName());
                }

                try {
                    Plugin plugin = (Plugin) descriptor.getMain().newInstance();
                    plugin.init(LoggerFactory.getLogger(descriptor.getName()), server, descriptor);
                    return plugin;
                } catch  (ReflectiveOperationException ex) {
                    throw new RuntimeException("Failed to create an instance of " + descriptor.getMain().getCanonicalName(), ex);
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
}
