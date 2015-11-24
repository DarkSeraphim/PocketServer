package com.pocketserver.world.generator;

import com.pocketserver.world.Biome;
import com.pocketserver.api.world.Chunk;

public abstract class ChunkPopulator {
    private final Biome biome;

    protected ChunkPopulator(Biome biome) {
        this.biome = biome;
    }

    public Biome getBiome() {
        return biome;
    }

    public abstract void generateChunk(Chunk chunk);
}
