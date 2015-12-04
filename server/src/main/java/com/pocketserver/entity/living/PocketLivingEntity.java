package com.pocketserver.entity.living;

import com.pocketserver.api.entity.living.LivingEntity;
import com.pocketserver.api.entity.living.Path;
import com.pocketserver.api.world.Location;
import com.pocketserver.entity.PocketEntity;

public class PocketLivingEntity extends PocketEntity implements LivingEntity {
    private double health;

    public PocketLivingEntity() {
        this.health = 20.00D;
    }

    @Override
    public void kill() {
        this.health = 0;
        // TODO: Send death packets & remove from memory
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public void move(Location location) {

    }

    @Override
    public void setPath(Path path) {

    }

}
