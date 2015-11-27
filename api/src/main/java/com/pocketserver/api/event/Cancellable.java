package com.pocketserver.api.event;

public interface Cancellable {
    /**
     * Return if the event was already cancelled by another plugin.
     *
     * @return if the event was cancelled or not.
     *
     * @see Cancellable
     */
    boolean isCancelled();

    /**
     * Set whether or not to cancel the event. Able to un-cancel and event or cancel
     * it for the first time.
     *
     * @param cancelled sets the event as cancelled or not.
     *
     * @see Cancellable
     */
    void setCancelled(boolean cancelled);
}
