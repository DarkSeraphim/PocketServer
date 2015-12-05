package com.pocketserver.api.event;

import io.netty.util.ResourceLeak;
import io.netty.util.ResourceLeakDetector;

public abstract class Event {
    static final ResourceLeakDetector<Event> leakDetector = new ResourceLeakDetector<>(Event.class);

    final ResourceLeak leak;

    protected Event() {
        this.leak = leakDetector.open(this);
    }

    void done() {
        if (leak != null) {
            leak.close();
        }
    }

    ResourceLeak getLeak() {
        return leak;
    }
}