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
    /**
     *
     * @return The chance the structure appears in percentages.
     * <ul>
     *     <li>If 0, the generator will <b>not</b> appear, ever.</li>
     *     <li>If 1-99 the generator will appear based on the percentage returned</li>
     *     <li>If 100, the generator will always appear</li>
     * </ul>
     */
    double getGeneratorChance();
    void generate(Chunk chunk, int x, int y, int z);
}
