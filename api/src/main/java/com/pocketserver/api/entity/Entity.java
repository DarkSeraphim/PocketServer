package com.pocketserver.api.entity;

import com.pocketserver.api.world.Location;
import com.pocketserver.api.world.Vector;

public interface Entity {

    int getEntityId();

    Vector getVelocity();

    void setVelocity(Vector velocity);

    Location getLocation();

    void teleport(Location location);

}