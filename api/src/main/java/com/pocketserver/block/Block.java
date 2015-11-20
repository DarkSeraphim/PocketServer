package com.pocketserver.block;

import com.pocketserver.world.Chunk;
import com.pocketserver.world.Location;

public interface Block {

    Material getType();

    byte getData();

    Location getLocation();

    Chunk getChunk();

    void setType(Material type);
}