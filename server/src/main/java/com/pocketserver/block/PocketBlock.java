package com.pocketserver.block;

import com.pocketserver.api.block.Block;
import com.pocketserver.api.block.Material;
import com.pocketserver.api.world.Chunk;
import com.pocketserver.api.world.Location;
import com.pocketserver.api.world.World;

public class PocketBlock implements Block {

    private final Location location;
    private volatile Material material;
    private volatile byte data;

    public PocketBlock(Material material, Location location, byte data) {
        this.material = material;
        this.location = location;
        this.data = data;
    }

    public PocketBlock(Material material, Location location) {
        this(material, location, (byte) 0);
    }

    public PocketBlock(Material material,World world, int x, int y, int z) {
        this(material, new Location(world, x, y, z));
    }

    public PocketBlock(Material material,World world, int x, int y, int z, byte data) {
        this(material, new Location(world, x, y, z), data);
    }

    @Override
    public Material getType() {
        return material;
    }

    @Override
    public void setType(Material type) {
        this.material = type;
    }

    @Override
    public byte getData() {
        return data;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Chunk getChunk() {
        return getLocation().getChunk();
    }
}
