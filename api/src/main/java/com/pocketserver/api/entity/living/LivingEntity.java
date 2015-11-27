package com.pocketserver.api.entity.living;

import com.pocketserver.api.entity.Entity;
import com.pocketserver.api.world.Location;

/**
 * All living entities such as Zombies, Players, and anything that moves by itself and can attack.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 *
 * @see Entity
 */
public interface LivingEntity extends Entity {

    /**
     * Sets the entities health to 0 and removes them from the game.
     */
    void kill();

    /**
     * Will retrieve the health of the current entity.
     *
     * @return health of entity.
     */
    double getHealth();

    /**
     * Sets the health of the entity. If set to 0 it will practically call the {@link #kill()} method
     * since the health can't be below 0.
     *
     * @param health the target health amount.
     */
    void setHealth(double health);

    /**
     * Moves the entity to the target location. Will generally be called by the server
     * automatically to move the entity on its normal path.
     *
     * @param location the target location.
     *
     * @see Path
     */
    void move(Location location);

    /**
     * Sets the current path for the entity to follow and move accordingly.
     *
     * @param path sets the current path to this.
     *
     * @see Path
     */
    void setPath(Path path);
}
