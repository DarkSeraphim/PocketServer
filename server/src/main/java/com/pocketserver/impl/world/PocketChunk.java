package com.pocketserver.impl.world;

import java.util.concurrent.atomic.AtomicBoolean;

import com.pocketserver.block.Block;
import com.pocketserver.block.Material;
import com.pocketserver.impl.block.PocketBlock;
import com.pocketserver.world.Chunk;
import com.pocketserver.world.Location;
import com.pocketserver.world.World;

public class PocketChunk implements Chunk {

    private final int x;
    private final int z;
    private final PocketWorld world;
    private final PocketBlock[] blocks = new PocketBlock[256 * 16 * 16];
    private AtomicBoolean updated;

    public PocketChunk(PocketWorld world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;

        for (int dx = 0; dx < 16; dx++) {
            for (int dy = 0; dy < 256; dy++) {
                for (int dz = 0; dz < 16; dz++) {
                    PocketBlock b = new PocketBlock(Material.AIR, new Location(world, x * 16 + dx, dy, z * 16 + dz));
                    // TODO: Load actual block
                    blocks[getBlockIndex(dx, dy, dz)] = b;
                }
            }
        }
    }

    public PocketChunk(Location loc) {
        this((PocketWorld) loc.getWorld(), loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }

    private int getBlockIndex(int x, int y, int z) {
        return (y << 8) + (x << 4) + z;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public Block getBlock(Location loc) {
        int dx = loc.getBlockX(), dz = loc.getBlockZ();
        if ((dx << 4) != x || (dz << 4) != z)
            throw new IllegalArgumentException("Location is not inside this Chunk.");
        return getBlock(dx & 0xF, loc.getBlockY(), dz & 0xF);
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        if (0 <= x && x < 16 && 0 <= y && y < 256 && 0 <= z && z < 16)
            return blocks[getBlockIndex(x, y, z)];
        return null;
    }

    @Override
    public boolean isInChunk(Location location) {
        int dx = location.getBlockX(), dz = location.getBlockZ();
        return  ((dx << 4) == x && (dz << 4) == z);
    }

    @Override
    public boolean isInChunk(int x, int y, int z) {
        return isInChunk(new Location(world,x,y,z));
    }

    @Override
    public void proposeUpdates() {
        //TODO: Send all the new blocks to client.
    }

    @Override
    public void queueUpdate(int x, int y, int z, Material material) {
        this.updated.set(true);

        blocks[getBlockIndex(x,y,z)].setType(material);
    }
}
