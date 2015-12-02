package com.pocketserver.net.packet;

import com.pocketserver.net.codec.Encapsulation;
import com.pocketserver.net.codec.EncapsulationStrategy;

public interface Encapsulated {
    default EncapsulationStrategy getEncapsulationStrategy() {
        return Encapsulation.BARE;
    }
}
