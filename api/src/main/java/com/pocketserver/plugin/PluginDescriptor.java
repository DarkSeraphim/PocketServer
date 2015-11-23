package com.pocketserver.plugin;

import com.google.common.base.Preconditions;

import java.util.Properties;

/**
 * A bean describing where to find the main {@link Plugin} class and what other plugins this may
 * depend on, also includes things such as the plugin version and author information.
 *
 * @author Connor Spencer Harries
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 *
 * @see PluginManager
 * @see Plugin
 */
public final class PluginDescriptor {
    /*
     * TODO: Implement possible "Semver" class for easy version comparison
     */
    private String name;
    private String version;
    private String author;
    private Class main;

    public PluginDescriptor() {

    }

    public PluginDescriptor(String name, String version, String author, Class main) {
        this.name = name;
        this.version = version;
        this.author = author;
    }

    public PluginDescriptor(Properties properties) {
        setVersion(verifyKey(properties, "version"));
        setAuthor(verifyKey(properties, "author"));
        setName(verifyKey(properties, "name"));
        try {
            setMain(Class.forName(verifyKey(properties, "main")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not locate main class", e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Class getMain() {
      return main;
    }

    public void setMain(Class main) {
      this.main = main;
    }

    static String verifyKey(Properties properties, String fieldName) {
        Preconditions.checkArgument(properties.containsKey(fieldName), "plugin descriptor must contain a \"%s\" field", fieldName);
        String value = Preconditions.checkNotNull(properties.getProperty(fieldName), "%s should not be null!", fieldName);
        Preconditions.checkArgument(value.length() > 0, "%s should not be empty!", fieldName);
        return value;
    }
}
