package com.pocketserver.api.event.entity;

import com.pocketserver.api.entity.Entity;
import com.pocketserver.api.world.Location;

public class EntityMoveEvent extends EntityEvent {
    private Location to;
    private final Location from;

    public EntityMoveEvent(Entity entity, Location to, Location from) {
        super(entity);
        this.to = to;
        this.from = from;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public Location getTo() {
        return to;
    }

    public Location getFrom() {
        return from;
    }
}
