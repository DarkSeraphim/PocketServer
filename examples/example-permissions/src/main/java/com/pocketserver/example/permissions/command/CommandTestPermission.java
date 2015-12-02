package com.pocketserver.example.permissions.command;

import java.util.Optional;

import com.pocketserver.api.ChatColor;
import com.pocketserver.api.Server;
import com.pocketserver.api.command.Command;
import com.pocketserver.api.command.CommandExecutor;
import com.pocketserver.api.player.Player;

public class CommandTestPermission extends Command {
    private final Server server;

    public CommandTestPermission(Server server) {
        super("test-permission");
        this.server = server;
    }

    @Override
    public void execute(CommandExecutor executor, String used, String[] args) {
        if (args.length < 2) {
            executor.sendMessage("Usage: /test-permission <username> <permission>");
        } else {
            Optional<? extends Player> possiblePlayer = server.getOnlinePlayers().stream()
                .filter(player -> player.getName().equalsIgnoreCase(args[0]))
                .findFirst();

            if (possiblePlayer.isPresent()) {
                Player player = possiblePlayer.get();
                if (player.hasPermission(args[1])) {
                    executor.sendMessage(ChatColor.GREEN + player.getName() + " has " + args[1]);
                } else {
                    executor.sendMessage(ChatColor.RED + player.getName() + " does not have " + args[1]);
                }
            } else {
                executor.sendMessage(ChatColor.RED + "Sorry, that only works on online players!");
            }
        }
    }
}
