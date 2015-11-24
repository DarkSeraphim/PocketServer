package com.pocketserver.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.pocketserver.api.Server;
import com.pocketserver.entity.living.PocketLivingEntity;
import com.pocketserver.net.packets.message.MessagePacket;
import com.pocketserver.api.player.GameMode;
import com.pocketserver.api.player.Player;

public class PocketPlayer extends PocketLivingEntity implements Player {

    private final Map<String, Boolean> permissions = new HashMap<>();
    private final PlayerConnection playerConnection;
    private boolean op;
    private String name;
    private GameMode gameMode = GameMode.SURVIVAL;

    public PocketPlayer(int entityId, PlayerConnection playerConnection) {
        super(entityId);
        this.playerConnection = playerConnection;
    }

    @Override
    public void sendMessage(String message) {
        new MessagePacket(message);
    }

    @Override
    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public InetSocketAddress getAddress() {
        return playerConnection.getAddress();
    }

    @Override
    public void chat(String message) {

    }

    @Override
    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean hasPermission(String permission) {
        if (permission.isEmpty()) {
            return true;
        }
        return Server.getServer().getPermissionResolver().checkPermission(this, permission);
    }

    @Override
    public void setPermission(String permission, boolean value) {
        /*
         * TODO: Possibly move elsewhere, doesn't make much sense based on how hasPermission implementation works.
         */
        permissions.put(Preconditions.checkNotNull(permission, "permission should not be null").toLowerCase(), value);
    }

    @Override
    public boolean isOp() {
        return op;
    }

    @Override
    public void setOp(boolean op) {
        this.op = op;
    }

    // TODO: Make less ugly, copying a Map on every invocation isn't awesome.
    public Map<String, Boolean> getPermissions() {
        /*
         * TODO: Possibly move elsewhere, doesn't make much sense based on how hasPermission implementation works.
         */
        return ImmutableMap.copyOf(permissions);
    }
}
