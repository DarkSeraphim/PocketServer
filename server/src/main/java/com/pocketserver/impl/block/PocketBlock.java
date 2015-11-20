package com.pocketserver.impl.block;

import com.pocketserver.block.Block;
import com.pocketserver.block.Material;
import com.pocketserver.world.Chunk;
import com.pocketserver.world.Location;

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

    @Override
    public Material getType() {
        return material;
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
