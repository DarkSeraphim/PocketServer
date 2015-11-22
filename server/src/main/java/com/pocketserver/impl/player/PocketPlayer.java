package com.pocketserver.impl.player;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.pocketserver.impl.entity.living.PocketLivingEntity;
import com.pocketserver.impl.net.packets.message.MessagePacket;
import com.pocketserver.player.GameMode;
import com.pocketserver.player.Player;

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
        boolean base = permissions.containsKey(permission) && permissions.get(permission);
        if (base)
            return true;
        while (permission.contains(".")) {
            permission = permission.substring(0, permission.lastIndexOf('.'));
            if (permissions.containsKey(permission + ".*") && permissions.get(permission + ".*"))
                return true;
        }
        return permissions.containsKey("*") ? permissions.get("*") : false;
    }

    @Override
    public void setPermission(String permission, boolean value) {
        Preconditions.checkNotNull(permission, "Permission cannot be null");
        permissions.put(permission, value);
    }

    @Override
    public boolean isOp() {
        return op;
    }

    @Override
    public void setOp(boolean op) {
        this.op = op;
    }
}
