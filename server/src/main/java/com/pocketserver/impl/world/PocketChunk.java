package com.pocketserver.impl.world;

import com.pocketserver.impl.block.PocketBlock;

public class PocketChunk {
    private final PocketWorld world;
    private final int xPosition;
    private final int zPosition;
    private final PocketBlock[] blocks = new PocketBlock[256*16*16];

    public PocketChunk(PocketWorld world, int xPosition, int zPosition) {
        this.world = world;
        this.xPosition = xPosition;
        this.zPosition = zPosition;
    }

    /*
    private final World world;
    private final int x;
    private final int z;

    private final Block[] blocks = new Block[256 * 16 * 16];

    public PocketChunk(World world, int cx, int cz) {
        this.world = world;
        this.x = cx;
        this.z = cz;

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    Block b = new Block(Material.AIR, new Location(world, x, y, z));
                    blocks[getBlockIndex(x, y, z)] = b;
                }
            }
        }
    }

    public PocketChunk(Location loc) {
        this(loc.getWorld(), loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Block getBlock(Location loc) {
        return getBlock(loc.getBlockX() & 0xF, loc.getBlockY(), loc.getBlockZ() & 0xF);
    }

    private int getBlockIndex(int x, int y, int z) {
        return (y << 8) + (x << 4) + z;
    }

    public Block getBlock(int x, int y, int z) {
        if (0 <= x && x < 16 && 0 <= y && y < 256 && 0 <= z && z < 16)
            return blocks[getBlockIndex(x, y, z)];
        return null;
    }
    */
}