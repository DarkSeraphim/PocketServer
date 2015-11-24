package com.pocketserver.api.block;

import com.pocketserver.api.world.Chunk;
import com.pocketserver.api.world.Location;

public interface Block {

    Material getType();

    byte getData();

    Location getLocation();

    Chunk getChunk();

    void setType(Material type);
}