package com.pocketserver.api.world;

import com.pocketserver.api.block.Block;

public interface World {

    Chunk getChunk(int x, int z);

    Chunk getChunk(int cx, int cz, boolean load);

    Block getBlockAt(int x, int y, int z);

    Block getBlockAt(Location location);
}
