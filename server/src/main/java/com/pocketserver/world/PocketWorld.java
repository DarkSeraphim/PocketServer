package com.pocketserver.world;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import com.pocketserver.api.block.Block;
import com.pocketserver.api.world.Chunk;
import com.pocketserver.api.world.Location;
import com.pocketserver.api.world.World;

public class PocketWorld implements World {

    private final Table<Integer, Integer, PocketChunk> chunks = HashBasedTable.create();

    @Override
    public PocketChunk getChunk(int x, int z) {
        return getChunk(x, z, false);
    }

    @Override
    public PocketChunk getChunk(int cx, int cz, boolean load) {
        if (!load || chunks.contains(cx, cz))
            return chunks.get(cx, cz);
        PocketChunk c = new PocketChunk(this, cx, cz);
        chunks.put(cx, cz, c);
        return c;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        int cx = x >> 4, cz = z >> 4;
        Chunk c = getChunk(cx, cz);
        return c != null ? c.getBlock(x % 16, y, z % 16) : null;
    }

    @Override
    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
