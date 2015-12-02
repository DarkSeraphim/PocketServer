package com.pocketserver.player;

import com.google.common.base.Preconditions;

import java.net.InetSocketAddress;

import com.pocketserver.api.Server;
import com.pocketserver.api.permissions.PermissionResolver;
import com.pocketserver.api.permissions.PermissionResolver.Result;
import com.pocketserver.api.player.GameMode;
import com.pocketserver.api.player.Player;
import com.pocketserver.entity.living.PocketLivingEntity;

public class PocketPlayer extends PocketLivingEntity implements Player {
    private final PlayerConnection playerConnection;
    private GameMode gameMode;
    private Server server;
    private String name;
    private boolean op;

    public PocketPlayer(int entityId, Server server, PlayerConnection playerConnection) {
        super(entityId);
        this.playerConnection = playerConnection;
        this.gameMode = GameMode.SURVIVAL;
        this.server = server;
    }

    @Override
    public void chat(String message) {

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
    public String getName() {
        return name;
    }

    @Override
    public void sendMessage(String message) {

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

        for (PermissionResolver resolver : server.getPermissionPipeline()) {
            Result result = resolver.checkPermission(this, permission);
            if (result == Result.ALLOW) {
                return true;
            } else if (result == Result.DENY) {
                return false;
            }
        }
        return false;
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
