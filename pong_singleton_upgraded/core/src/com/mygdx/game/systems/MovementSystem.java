package com.mygdx.game.systems;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.components.Component;
import com.mygdx.game.components.RectangleComponent;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.observers.EntityChangeObserver;

public class MovementSystem implements EntityChangeObserver {
    public void update(Entity entity) {
        Rectangle rectangle = entity.getComponent(RectangleComponent.class).rectangle;
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
        if(velocity != null){
            rectangle.x += velocity.x;
            rectangle.y += velocity.y;
        }
    }
    @Override
    public void notifyEntityChange(Entity entity) {
        update(entity);
    }
}