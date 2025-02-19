package com.mygdx.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.enums.Shape;

public class ShapeComponent extends Component {
    private Shape shapeType;
    private Color color;

    public ShapeComponent(Shape shapeType, Color color) {
        this.shapeType = shapeType;
        this.color = color;
    }

    public Shape getShapeType() {
        return shapeType;
    }

    public void setShapeType(Shape shapeType) {
        this.shapeType = shapeType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
