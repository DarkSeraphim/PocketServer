package com.pocketserver.api.world;

public class Vector implements Cloneable {

    private final double x, y, z;

    public Vector() {
        this(0, 0, 0);
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Vector add(double x, double y, double z) {
        return new Vector(this.x + x, this.y + y, this.z + z);
    }

    public Vector add(Vector vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vector multiply(double multiple) {
        return new Vector(x * multiple, y * multiple, z * multiple);
    }

    public Vector dot(Vector vec) {
        return new Vector(x * vec.x, y * vec.y, z * vec.z);
    }

    public Vector cross(Vector vec) {
        return new Vector(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
    }

    @Override
    public Vector clone() {
        return new Vector(x, y, z);
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }

    public Location toLocation(World world, float yaw, float pitch) {
        return new Location(world, x, y, z, yaw, pitch);
    }

}
