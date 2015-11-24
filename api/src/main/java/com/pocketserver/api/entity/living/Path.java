package com.pocketserver.api.entity.living;

import com.pocketserver.api.world.Location;

public abstract class Path {
    private final Location startPoint;
    private final Location endPoint;
    private Location currentPoint;
    private Location nextPoint;

    public Path(Location startPoint, Location endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public abstract void move();

    public Location getStartPoint() {
        return startPoint;
    }

    public Location getEndPoint() {
        return endPoint;
    }

    public Location getCurrentPoint() {
        return currentPoint;
    }

    protected void setCurrentPoint(Location currentPoint) {
        this.currentPoint = currentPoint;
    }
}
