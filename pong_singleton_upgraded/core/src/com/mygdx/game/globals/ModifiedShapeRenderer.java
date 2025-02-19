package com.mygdx.game.globals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.components.RectangleComponent;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.components.ShapeComponent;
import com.mygdx.game.enums.Shape;

public class ModifiedShapeRenderer extends ShapeRenderer {
    public void draw(Entity entity){
        Shape shape = entity.getComponent(ShapeComponent.class).getShapeType();
        Color color = entity.getComponent(ShapeComponent.class).getColor();
        Rectangle rectangle = entity.getComponent(RectangleComponent.class).rectangle;

        this.begin(ModifiedShapeRenderer.ShapeType.Filled);
		this.setColor(color);

        switch (shape){
            case rect:
                this.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                break;
            case circle:
                this.circle(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2, rectangle.width / 2);
                break;
        }

        this.end();
    }
}
