package com.pocketserver.api.event.entity.vehicle;

import com.pocketserver.api.event.Cancellable;
import com.pocketserver.api.event.Event;

public class VehicleDismountEvent extends Event implements Cancellable {
    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
