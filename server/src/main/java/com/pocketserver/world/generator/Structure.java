package com.pocketserver.world.generator;

import com.pocketserver.api.world.Chunk;

/**
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 *
 * @deprecated This will probably not remain constant or even remotely similar. Implement at
 * own risk.
 */

//TODO: Fix all of the structurs and migrate towards a permanent system.
@Deprecated
public interface Structure {
    double getGeneratorChance();
    void generate(Chunk chunk, int x, int y, int z);
}
