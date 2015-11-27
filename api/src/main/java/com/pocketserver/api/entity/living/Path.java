package com.pocketserver.api.entity.living;

import com.pocketserver.api.world.Location;

/**
 * Defines a path for entities to move at. This will be the entities equivalent of a PathfindingGoal so that
 * they can move around and have an AI as well as support for custom paths.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public abstract class Path {
    private final Location startPoint;
    private final Location endPoint;
    private Location currentPoint;
    private Location nextPoint;

    /**
     * The locations for a living entity to move to.
     *
     * @param startPoint original starting Location.
     * @param endPoint the point for the entity to move to.
     *
     * @see Location
     */
    public Path(Location startPoint, Location endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    /**
     * Called by the entities for when they're supposed to move.
     */
    public abstract void move();

    /**
     * Retrieves the field that was originally set by the constructor of path.
     *
     * @return the start point set in the constructor.
     *
     * @see Location
     */
    public Location getStartPoint() {
        return startPoint;
    }

    /**
     * Retrieves the field that was originally set by the constructor of path.
     *
     * @return the ending point set in the constructor.
     *
     * @see Location
     */
    public Location getEndPoint() {
        return endPoint;
    }

    /**
     * Retrieves the current position of the entity. This is useful for multiple step movements where
     * the entity will have to move several times at once.
     *
     * @return the current Location fo the entity.
     *
     * @see Location
     */
    public Location getCurrentPoint() {
        return currentPoint;
    }

    /**
     * Updates the progress of the path in order to keep a maintained area for the entity.
     *
     * @param currentPoint sets the current point of the path/entity location.
     */
    protected void setCurrentPoint(Location currentPoint) {
        this.currentPoint = currentPoint;
    }
}
