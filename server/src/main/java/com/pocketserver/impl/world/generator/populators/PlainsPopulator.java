package com.pocketserver.impl.world.generator.populators;

import com.pocketserver.block.Material;
import com.pocketserver.impl.world.Biome;
import com.pocketserver.impl.world.generator.ChunkPopulator;
import com.pocketserver.world.Chunk;

public class PlainsPopulator extends ChunkPopulator {

    protected PlainsPopulator() {
        super(Biome.PLAINS);
    }

    @Override
    public void generateChunk(Chunk chunk) {
        for (int x = 0; x < chunk.getX(); x++) {
            for (int z = 0; z < chunk.getZ(); z++) {
                chunk.queueUpdate(x, 0, z, Material.STONE);
            }
        }
        chunk.proposeUpdates();
    }
}
