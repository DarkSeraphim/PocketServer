package com.pocketserver.api.entity.vehicle;

import com.pocketserver.api.entity.living.LivingEntity;

/**
 * Provides base methods for rideable entities. Almost all entities are rideable however
 * not all of them are specifically vehicles.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public interface Rideable {
    void setPassenger(LivingEntity entity);

    LivingEntity getPassenger();
}
