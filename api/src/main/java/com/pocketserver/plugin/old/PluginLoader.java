package com.pocketserver.plugin.old;

public class PluginLoader {
/*
    private final Set<URLClassLoader> loaders = new HashSet<>();
    private final Stack<Plugin> plugins = new Stack<>();

    public Stack<Plugin> loadPlugins() {
        if (!plugins.isEmpty()) // already loaded!
            return plugins;
        File pluginDirectory = new File("plugins");
        if (!pluginDirectory.exists()) {
            pluginDirectory.mkdir();
            return new Stack<>(); // No plugins so do not bother
        }

        File[] files = pluginDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".jar");
            }
        });
        Set<Plugin> found = new HashSet<>();
        for (File file : files) {
            Set<Plugin> plgs = loadPlugins(file);
            if (plgs.size() > 0) {
                Server.getServer().getLogger().info(String.format("Found %d plugins in %s: %s", plgs.size(), file.getName(), plgs.stream().map(p -> p.getName()).collect(Collectors.toList())));
                found.addAll(plgs);
            }
        }
        Map<String, Integer> duplicates = new HashMap<>();
        Map<String, Plugin> names = new HashMap<>();
        Map<String, Set<String>> deps = new WeakHashMap<>();
        found.forEach(p -> {
            if (p.getName() == null || p.getName().isEmpty()) {
                Server.getServer().getLogger().warn("Found a plugin with a null or empty plugin name. Skipping.");
            } else if (names.containsKey(p.getName())) {
                duplicates.put(p.getName(), duplicates.getOrDefault(p.getName(), 1) + 1);
            } else {
                names.put(p.getName(), p);
                deps.put(p.getName(), Sets.newHashSet(p.getDependencies()));
            }
        });
        found.removeIf(p -> p.getName() == null || p.getName().isEmpty());
        duplicates.forEach((s, i) -> {
            Server.getServer().getLogger().warn(String.format("Found %d occurrences of plugin %s. Not loading any.", i, s));
            names.remove(s);
            deps.remove(s);
        });
        found.removeIf(p -> duplicates.containsKey(p.getName()));
        Set<String> missingDeps = new HashSet<>();
        for (Plugin p : found) {
            Set<String> missing = deps.get(p.getName()).stream().filter(d -> !deps.containsKey(d)).collect(Collectors.toSet());
            if (!missing.isEmpty())
                missingDeps.add(p.getName());
        }
        missingDeps.forEach(p -> {
            Server.getServer().getLogger().warn(String.format("Error loading plugin %s: Dependencies missing: %s", p, deps.get(p).toString()));
            names.remove(p);
            deps.remove(p);
        });
        found.removeIf(p -> missingDeps.contains(p.getName()));
        Set<String> circular = getCircularDependencies(found, deps);
        circular.forEach(p -> {
            Server.getServer().getLogger().warn(String.format("Error loading plugin %s: Circular dependencies detected.", p));
            names.remove(p);
            deps.remove(p);
        });
        found.removeIf(p -> circular.contains(p.getName()));
        int numLoaded;
        do {
            numLoaded = 0;
            List<Plugin> nodeps = found.stream().filter(p -> deps.get(p.getName()).isEmpty()).collect(Collectors.toList());
            for (Plugin p : nodeps) {
                try {
                    p.logger = LoggerFactory.getLogger(p.getName());
                    found.remove(p);
                    p.onLoad();
                    deps.forEach((s, l) -> {
                        l.remove(p.getName());
                        deps.put(s, l);
                    });
                    plugins.add(p);
                    numLoaded++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } while (numLoaded > 0);
        found.forEach(p -> {
            Server.getServer().getLogger().warn(String.format("Error loading plugin %s: Dependencies missing: %s", p.getName(), deps.get(p.getName()).toString()));
        });
        plugins.forEach(Plugin::onEnable);
        return plugins;
    }

    public Set<String> getCircularDependencies(Set<Plugin> search, Map<String, Set<String>> deps) {
        Map<String, Plugin> names = search.stream().collect(Collectors.toMap(p -> p.getName(), p -> p));
        Set<String> circular = new HashSet<>();
        for (Plugin plugin : search) {
            Set<String> upstream = new HashSet<>();
            Stack<String> toSearch = new Stack<>();
            toSearch.add(plugin.getName());
            while (!toSearch.isEmpty()) {
                String pn = toSearch.pop();
                Plugin plg = names.get(pn);
                if (plg != null && deps.containsKey(pn)) {
                    deps.get(pn).stream().filter(s -> !upstream.contains(s)).forEach(s -> toSearch.add(s));
                    upstream.addAll(deps.get(pn));
                }
            }
            if (upstream.contains(plugin.getName()))
                circular.add(plugin.getName());
        }
        return circular;
    }

    private Set<Plugin> loadPlugins(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            URLClassLoader cl = new URLClassLoader(new URL[] { file.toURI().toURL() });
            loaders.add(cl);
            Set<Plugin> found = new HashSet<>();
            Enumeration<JarEntry> entries = jarFile.entries();
            double highest = 0;
            while (entries.hasMoreElements()) {
                String entry = entries.nextElement().getName();
                if (entry.endsWith(".class")) {
                    try {
                        Class<?> clazz = cl.loadClass(entry.replace('/', '.').substring(0, entry.length() - 6));
                        if (Plugin.class.isAssignableFrom(clazz)) {
                            try {
                                found.add(clazz.asSubclass(Plugin.class).newInstance());
                            } catch (InstantiationException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (UnsupportedClassVersionError error) {
                        try {
                            String[] words = error.getMessage().trim().split("\\s+");
                            double ver = Double.parseDouble(words[words.length - 1]);
                            highest = Math.max(ver, highest);
                        } catch (Exception ex) {
                            error.printStackTrace();
                        }
                    } catch (ClassNotFoundException ignored) {
                    }
                }
            }
            if (highest == 0)
                if (found.isEmpty())
                    Server.getServer().getLogger().warn(String.format("No classes extending Plugin found in file %s. Not loading.", file.getName()));
                else {
                    found.forEach(p -> p.file = file);
                    return found;
                }
            else {
                String version;
                if (highest == 52.0)
                    version = "Java 8";
                else if (highest == 53.0)
                    version = "Java 9";
                else
                    version = "an unsupported version of Java";
                Server.getServer().getLogger().warn(String.format("Not loading plugins in file %s because it requires %s.", file.getName(), version));
            }
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    public void disablePlugins() {
        while (plugins.size() > 0) {
            Plugin plg = plugins.pop();
            plg.onDisable();
        }
        plugins.clear();
        loaders.clear();
    }
*/
}
