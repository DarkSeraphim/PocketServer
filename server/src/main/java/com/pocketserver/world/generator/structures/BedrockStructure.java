package com.pocketserver.world.generator.structures;

import com.pocketserver.api.block.Material;
import com.pocketserver.api.world.Chunk;
import com.pocketserver.world.generator.Structure;

public class BedrockStructure implements Structure {
    @Override
    public double getGeneratorChance() {
        return 100;
    }

    @Override
    public void generate(Chunk chunk, int x, int y, int z) {
        if (y == 0) {
            chunk.queueUpdate(x,y,z, Material.BEDROCK);
        }
    }
}
