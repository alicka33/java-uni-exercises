package com.mygdx.game.entities;

import com.mygdx.game.components.Component;
import com.mygdx.game.enums.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    String name;
    EntityType entityType;
    private List<Component> components = new ArrayList<>();
    public Entity(String name, EntityType entityType){
        this.name = name;
        this.entityType = entityType;
    }

    public void add(Component component) {
        components.add(component);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isInstance(component)) {
                return componentClass.cast(component);
            }
        }
        return null;
    }
    public EntityType getEntityType(){
        return entityType;
    }
    public String getName(){
        return name;
    }
}