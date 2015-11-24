package com.pocketserver.api.entity.vehicle;

import com.pocketserver.api.entity.living.LivingEntity;

public interface Rideable {
    void setPassenger(LivingEntity entity);

    LivingEntity getPassenger();
}
