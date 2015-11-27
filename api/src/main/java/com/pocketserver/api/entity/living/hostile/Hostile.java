package com.pocketserver.api.entity.living.hostile;

import com.pocketserver.api.entity.living.LivingEntity;

/**
 * All hostile mobs should be able to attack another entity.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public interface Hostile {
    /**
     * Attacks with entity. Able to be implemented to do a set amount of damage or possible a range.
     *
     * @param entity the entity to attack.
     */
    void attack(LivingEntity entity);
}
