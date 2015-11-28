package com.pocketserver.world.generator.populators;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.pocketserver.api.block.Material;
import com.pocketserver.api.world.Chunk;
import com.pocketserver.world.Biome;
import com.pocketserver.world.generator.ChunkPopulator;
import com.pocketserver.world.generator.structures.BedrockStructure;

public class DesertPopulator extends ChunkPopulator {

    public DesertPopulator() {
        super(Biome.DESERT, new BedrockStructure());
    }

    @Override
    public void generateChunk(Chunk chunk) {
        Random random = ThreadLocalRandom.current();
        for (int x = 0; x < chunk.getX(); x++) {
            for (int z = 0; z < chunk.getZ(); z++) {
                for (int y = 0; y < 50; y++) {
                    this.checkPopulators(chunk, x,y,z, random);
                    chunk.queueUpdate(x,y,z, Material.SAND);
                }
            }
        }
        chunk.proposeUpdates();
    }
}
