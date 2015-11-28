package com.pocketserver.world.generator;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.pocketserver.api.world.Chunk;
import com.pocketserver.world.Biome;

//TODO: Fix all of the chunk populators.
public abstract class ChunkPopulator {
    private final Biome biome;
    private final List<Structure> structures;

    protected ChunkPopulator(Biome biome) {
        this.biome = biome;
        this.structures = new ArrayList<>();
    }

    protected ChunkPopulator(Biome biome, Structure... structures) {
        this.biome = biome;
        this.structures = Arrays.asList(structures);
    }

    public final Biome getBiome() {
        return biome;
    }

    public abstract void generateChunk(Chunk chunk);

    public List<Structure> getStructures() {
        return ImmutableList.copyOf(structures);
    }

    protected void checkPopulators(Chunk chunk, int x, int y, int z) {
        this.checkPopulators(chunk, x, y, z, ThreadLocalRandom.current());
    }

    protected void checkPopulators(Chunk chunk, int x, int y, int z, Random random) {
        structures.stream().filter(s -> s.getGeneratorChance() > random.nextInt(99) + 1).forEach(s -> s.generate(chunk, x,y, z));
    }
}
