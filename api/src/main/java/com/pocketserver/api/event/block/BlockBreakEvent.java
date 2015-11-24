package com.pocketserver.api.event.block;

import com.pocketserver.api.block.Block;
import com.pocketserver.api.event.Cancellable;
import com.pocketserver.api.event.player.PlayerEvent;
import com.pocketserver.api.player.Player;
import com.pocketserver.api.world.Location;

public class BlockBreakEvent extends PlayerEvent implements Cancellable {
    private final Block block;
    private boolean cancelled;

    public BlockBreakEvent(Player player, Block block) {
        super(player);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public Location getLocation() {
        return block.getLocation();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
