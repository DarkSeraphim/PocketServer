package com.pocketserver.command;

import com.google.common.base.Joiner;
import com.pocketserver.api.ChatColor;
import com.pocketserver.api.command.Command;
import com.pocketserver.api.command.CommandExecutor;
import com.pocketserver.api.plugin.Plugin;
import com.pocketserver.api.plugin.PluginManager;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CommandPlugins extends Command {
    private static final Collector<CharSequence, ?, String> JOINER = Collectors.joining(ChatColor.GREEN + "Plugins [",
                                                                                        ChatColor.RESET + ", ",
                                                                                        ChatColor.GREEN + "]");

    private PluginManager manager;

    public CommandPlugins(PluginManager manager) {
        super("plugins", "pocket.command.plugins", "pl");
        this.manager = manager;
    }

    @Override
    public void execute(CommandExecutor executor, String[] args) {
        String join = manager.getPlugins().stream().map(this::formatName).collect(JOINER);
        executor.sendMessage(join);
    }

    private String formatName(Plugin plugin) {
        return (plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + plugin.getName();
    }
}
