package com.pocketserver.player;

import com.google.common.collect.ImmutableList;
import com.pocketserver.api.player.Player;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerRegistry {

    private static final PlayerRegistry INSTANCE = new PlayerRegistry();
    private final Map<InetSocketAddress, PocketPlayer> playerMap = new ConcurrentHashMap<>();

    public static PlayerRegistry get() {
        return INSTANCE;
    }

    public void registerPlayer(PocketPlayer player) {
        this.playerMap.put(player.getAddress(), player);
    }

    public void unregisterPlayer(PocketPlayer player) {
        this.playerMap.remove(player.getAddress());
    }

    public PocketPlayer getPlayer(InetSocketAddress address) {
        return playerMap.get(address);
    }

    public Optional<PocketPlayer> getPlayer(String name) {
        return playerMap.values().stream().filter(p -> p.getName().equalsIgnoreCase(name)).findAny();
    }

    public List<? extends Player> getPlayers() {
        return ImmutableList.copyOf(playerMap.values());
    }
}
