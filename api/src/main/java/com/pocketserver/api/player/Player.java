package com.pocketserver.api.player;

import java.net.InetSocketAddress;

import com.pocketserver.api.command.CommandExecutor;
import com.pocketserver.api.entity.living.LivingEntity;

public interface Player extends LivingEntity, CommandExecutor {

    void chat(String message);

    GameMode getGameMode();

    void setGameMode(GameMode mode);

    InetSocketAddress getAddress();

}
