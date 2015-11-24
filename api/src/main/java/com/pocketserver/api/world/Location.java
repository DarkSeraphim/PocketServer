package com.pocketserver.api.world;

import java.io.Serializable;

import com.pocketserver.api.block.Block;

public class Location implements Cloneable, Serializable {

    private static final long serialVersionUID = -7074605674263137642L;

    private final World world;
    private final double x, y, z;
    private final float yaw, pitch;

    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location(World world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public Location(Location location) {
        this(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    public Block getBlock() {
        return world.getBlockAt(this);
    }

    public World getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public int getBlockX() {
        return (int) x;
    }

    public int getBlockY() {
        return (int) y;
    }

    public int getBlockZ() {
        return (int) z;
    }

    public Location add(double x, double y, double z) {
        return new Location(world, this.x + x, this.y + y, this.z + z, yaw, pitch);
    }

    @Override
    protected Location clone() throws CloneNotSupportedException {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public Chunk getChunk() {
        return this.world.getChunk((int)x,(int)z,true);
    }
}
