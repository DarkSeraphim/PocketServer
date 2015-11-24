package com.pocketserver.api.event.entity;

import com.pocketserver.api.entity.Entity;
import com.pocketserver.api.event.Event;

public class EntityEvent extends Event {
    private final Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
