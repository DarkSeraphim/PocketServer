package com.pocketserver.api.event.block;

import com.pocketserver.api.block.Block;
import com.pocketserver.api.event.Cancellable;
import com.pocketserver.api.event.player.PlayerEvent;
import com.pocketserver.api.player.Player;
import com.pocketserver.api.world.Location;

/**
 * Called when a player breaks a block.
 *
 * @author TheLightMC
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class BlockBreakEvent extends PlayerEvent implements Cancellable {
    private final Block block;
    private boolean cancelled;

    /**
     * Constructor for when a player broke the block.
     *
     * @param player who broke the block.
     * @param block the block that was actually broken.
     */
    public BlockBreakEvent(Player player, Block block) {
        super(player);
        this.block = block;
    }

    /**
     * Gets the block that was broken.
     *
     * @return broken block.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Gets the location from the block that was broken and
     * then returns it.
     *
     * @return location of broken block.
     */
    public Location getLocation() {
        return block.getLocation();
    }

    /**
     * Return if the event was already cancelled by another plugin.
     *
     * @return if the event was cancelled or not.
     *
     * @see Cancellable
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the event to cancelled or not. Able to override the previously set value.
     *
     * @param cancelled sets the event as cancelled or not.
     *
     * @see Cancellable
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
