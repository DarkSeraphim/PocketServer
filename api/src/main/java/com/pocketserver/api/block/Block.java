package com.pocketserver.api.block;

import com.pocketserver.api.world.Chunk;
import com.pocketserver.api.world.Location;

/**
 * Provides a simple api to access blocks.
 * Allows for the editing of blocks such as setting them to different types.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public interface Block {

    /**
     * Returns the material of the block. Can be anyone of the material enums.
     * Will always return a value however the default value will always be Material.AIR.
     *
     * @return the material  type of the block
     * @see Material
     */
    Material getType();

    /**
     * Will automatically set the block type for the server and update it on the client automatically.
     *
     * @param material desired material for the Block
     * @see Material
     */
    void setType(Material material);

    /**
     * The blocks data that generally will correspond with {@link Material}.
     * Defaults to 0 if no specific data is set.
     *
     * @return the specific
     */
    byte getData();

    /**
     * The specific location of the block.
     * This location is immutable and can not be changed.
     *
     * @return location of the block.
     * @see Location
     */
    Location getLocation();

    /**
     * The chunk that the block is contained in. All blocks are in 16x16 chunks and
     * will always return a value.
     *
     * @return corresponding chunk that block is located in.
     * @see Chunk
     */
    Chunk getChunk();
}