package com.pocketserver.api.entity.living;

import com.pocketserver.api.entity.Entity;
import com.pocketserver.api.world.Location;

public interface LivingEntity extends Entity {

    void kill();

    double getHealth();

    void setHealth(double health);

    void move(Location location);

    void setPath(Path path);

    void setFoodLevel(double foodLevel);

    double getFootLevel();
}
