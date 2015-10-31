package com.pocketserver.player;

import java.net.InetAddress;

import com.pocketserver.entity.living.LivingEntity;

public class Player extends LivingEntity {
	private final InetAddress address;
    private GameMode gameMode = GameMode.SURVIVAL;
    private String name;

    public Player(int entityId, InetAddress address) {
    	super(entityId);
        this.address = address;
    }

    public void sendMessage() {

    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void chat(String message) {

    }

    public InetAddress getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}