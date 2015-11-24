package com.pocketserver.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.net.InetSocketAddress;
import java.util.Collection;

import com.pocketserver.api.Server;
import com.pocketserver.api.player.GameMode;
import com.pocketserver.api.player.Player;
import com.pocketserver.entity.living.PocketLivingEntity;
import com.pocketserver.net.packets.message.MessagePacket;

public class PocketPlayer extends PocketLivingEntity implements Player {
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
        Preconditions.checkArgument(this.name == null, "name cannot be reassigned once set!");
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
        Server.getServer().getPermissionResolver().setPermission(this, permission, value);
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
