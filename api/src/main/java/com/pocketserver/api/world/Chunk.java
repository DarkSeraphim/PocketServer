package com.pocketserver.api.world;

import com.pocketserver.api.block.Block;
import com.pocketserver.api.block.Material;

public interface Chunk {

    World getWorld();

    int getX();

    int getZ();

    Block getBlock(Location loc);

    Block getBlock(int dx, int dy, int dz);

    boolean isInChunk(Location location);

    boolean isInChunk(int x,int y, int z);

    void queueUpdate(int x, int y, int z, Material material);

    void proposeUpdates();
}