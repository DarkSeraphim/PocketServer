package com.pocketserver.impl.entity.living;

import com.pocketserver.entity.living.LivingEntity;
import com.pocketserver.entity.living.Path;
import com.pocketserver.impl.entity.PocketEntity;
import com.pocketserver.world.Location;

public class PocketLivingEntity extends PocketEntity implements LivingEntity {

    private double health;
    private double foodLevel;

    public PocketLivingEntity(int entityId) {
        super(entityId);
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

    @Override
    public void setFoodLevel(double foodLevel) {
        this.foodLevel = foodLevel;
    }

    @Override
    public double getFootLevel() {
        return foodLevel;
    }

    @Override
    public void kill() {
        this.health = 0;
        // TODO: Send death packets & remove from memory
    }

}
