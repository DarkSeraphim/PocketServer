package com.pocketserver.player;

import com.google.common.base.Preconditions;

import java.net.InetSocketAddress;

import com.pocketserver.api.Server;
import com.pocketserver.api.permissions.PermissionResolver;
import com.pocketserver.api.permissions.PermissionResolver.Result;
import com.pocketserver.api.player.GameMode;
import com.pocketserver.api.player.Player;
import com.pocketserver.entity.living.PocketLivingEntity;
import com.pocketserver.net.Packet;
import io.netty.channel.Channel;

public class PocketPlayer extends PocketLivingEntity implements Player {
    private final InetSocketAddress address;
    private final Channel channel;
    private final Server server;
    private final Unsafe unsafe;

    private GameMode gameMode;
    private String name;
    private boolean op;

    public PocketPlayer(int entityId, Server server, Channel channel, InetSocketAddress address) {
        super(entityId);
        this.gameMode = GameMode.SURVIVAL;
        this.channel = channel;
        this.address = address;
        this.server = server;

        this.unsafe = new Unsafe() {
            @Override
            public void send(Packet packet) {
                PocketPlayer.this.channel.writeAndFlush(packet);
            }
        };
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
        return address;
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

    public Unsafe unsafe() {
        return unsafe;
    }

    public interface Unsafe {
        void send(Packet packet);
    }
}
