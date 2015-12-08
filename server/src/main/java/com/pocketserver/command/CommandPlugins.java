package com.pocketserver.command;

import com.google.common.base.Joiner;
import com.pocketserver.api.ChatColor;
import com.pocketserver.api.command.Command;
import com.pocketserver.api.command.CommandExecutor;
import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.api.plugin.PluginManager;

import java.util.stream.Collectors;

public class CommandPlugins extends Command {
    private final Joiner joiner = Joiner.on(ChatColor.RESET + ", ");
    private PluginManager manager;

    public CommandPlugins(PluginManager manager) {
        super("plugins", "pocket.command.plugins", "pl");
        this.manager = manager;
    }

    @Override
    public void execute(CommandExecutor executor, String[] args) {
        String join = joiner.join(manager.getPlugins().stream().map(this::formatName).collect(Collectors.toList()));
        executor.sendMessage(ChatColor.GREEN + "Plugins [" + join + ChatColor.GREEN + "]");
    }

    private String formatName(Plugin plugin) {
        String name = plugin.getName();
        ChatColor color = plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED;
        return color + name;
    }
}
