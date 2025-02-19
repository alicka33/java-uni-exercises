package com.mygdx.game.observers;

import com.mygdx.game.components.Component;
import com.mygdx.game.entities.Entity;

public interface EntityChangeObserver {
    void notifyEntityChange(Entity component);
}
