package com.pocketserver.api.player;

import java.net.InetSocketAddress;
import java.util.UUID;

import com.pocketserver.api.command.CommandExecutor;
import com.pocketserver.api.entity.living.LivingEntity;

public interface Player extends LivingEntity, CommandExecutor {

    void chat(String message);

    Gamemode getGamemode();

    void setGamemode(Gamemode mode);

    InetSocketAddress getAddress();
}
