package com.pocketserver.api.entity;

import com.pocketserver.api.world.Location;
import com.pocketserver.api.world.Vector;

/**
 * The primary interface that all Entities will implement. All living things or items will
 * be of the type Entity. Provides some simple methods that can be used on any entitiy.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public interface Entity {

    /**
     * Returns the unique entity ID for the EntityType.
     *
     * @return unique entity ID.
     */
    int getEntityId();

    /**
     * Gets the speed/velocity that an entity is moving.
     *
     * @return velocity of an entity.
     *
     * @see Vector
     */
    Vector getVelocity();

    /**
     * Sets the velocity for the entity generally making it move or stop moving as
     * per the instructions of the velocity class.
     *
     * @param velocity the velocity to set for the entity.
     *
     * @see Vector
     */
    void setVelocity(Vector velocity);

    /**
     * The location of the entity.
     *
     * @return location that the entity is at.
     *
     * @see Location
     */
    Location getLocation();

    /**
     * Sets the entitys location by effectively teleporting it to that location.
     *
     * @param location target location for the entity
     *
     * @see Location
     */
    void teleport(Location location);

}