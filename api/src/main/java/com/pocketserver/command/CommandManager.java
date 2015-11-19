package com.pocketserver.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Preconditions;
import com.pocketserver.plugin.Plugin;

public class CommandManager {

    private final Map<String, Command> commands = new ConcurrentHashMap<>();
    private final Map<String, String> commandPlugins = new ConcurrentHashMap<>();
    private final Map<String, List<String>> pluginCommands = new ConcurrentHashMap<>();

    public void registerCommand(Command command, Plugin plugin) {
        Preconditions.checkNotNull(command, "Command cannot be null");
        Preconditions.checkNotNull(plugin, "Plugin cannot be null");
        List<String> cmds = pluginCommands.get(plugin.getName());
        if (cmds == null)
            cmds = new ArrayList<>();
        for (String alias : command.getAliases()) {
            cmds.add(alias.toLowerCase());
            commands.put(alias.toLowerCase(), command);
            commandPlugins.put(alias.toLowerCase(), plugin.getName());
        }
        pluginCommands.put(plugin.getName(), cmds);
    }

    public void unregisterCommand(Command command) {
        Preconditions.checkNotNull(command, "Command cannot be null");
        String[] aliases = command.getAliases();
        for (String s : aliases) {
            commands.remove(s);
            List<String> cmds = pluginCommands.get(commandPlugins.get(s));
            if (cmds != null) {
                cmds.remove(s);
                pluginCommands.put(commandPlugins.get(s), cmds);
            }
            commands.remove(s);
            commandPlugins.remove(s);
        }
    }

    public void executeCommand(CommandExecutor executor, String commandName) {
        synchronized (this) {
            Preconditions.checkNotNull(executor, "CommandExecutor cannot be null");
            Preconditions.checkNotNull(commandName, "Command string cannot be null");

            String[] arguments = commandName.split(" ");
            String label = arguments[0];
            Command command = getCommand(label);
            command.executeCommand(executor, label, Arrays.copyOfRange(arguments,1,arguments.length));
        }
    }

    private Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
