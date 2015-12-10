package com.pocketserver.player;

import com.google.common.base.Objects;

import java.net.InetSocketAddress;
import java.util.UUID;

import com.pocketserver.PocketServer;
import com.pocketserver.api.Server;
import com.pocketserver.api.event.player.PlayerChatEvent;
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
    private final String name;

    private GameMode gamemode;
    private boolean op;

    public PocketPlayer(PocketServer server, Channel channel, InetSocketAddress address, String name) {
        this.gamemode = GameMode.SURVIVAL;
        this.channel = channel;
        this.address = address;
        this.server = server;
        this.name = name;

        this.unsafe = new Unsafe() {
            @Override
            public void send(Packet packet) {
                PocketPlayer.this.channel.writeAndFlush(packet);
            }
        };
    }

    @Override
    public void chat(String message) {
        PocketServer server = (PocketServer) Server.getServer();
        PlayerChatEvent event = new PlayerChatEvent(this, message);
        server.getPluginManager().post(event);
        if (event.isCancelled()) {
            return;
        }
        //server.broadcast();
    }

    @Override
    public GameMode getGamemode() {
        return this.gamemode;
    }

    @Override
    public void setGamemode(GameMode gamemode) {
        this.gamemode = gamemode;
    }

    @Override
    public InetSocketAddress getAddress() {
        return this.address;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasPermission(String permission) {
        if (permission.isEmpty()) {
            return true;
        }

        for (PermissionResolver resolver : server.getPermissionPipeline()) {
            Result result = resolver.checkPermission(this, permission);
            if (result == Result.ALLOW || result == Result.DENY) {
                return result == Result.ALLOW;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj instanceof PocketPlayer) {
            PocketPlayer that = (PocketPlayer) obj;
            return name.equals(that.name) && that.address.equals(address);
        }
        return false;
    }

    public interface Unsafe {
        void send(Packet packet);
    }
}
