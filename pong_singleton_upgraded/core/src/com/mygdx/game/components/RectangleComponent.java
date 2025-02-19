package com.mygdx.game.components;


import com.badlogic.gdx.math.Rectangle;

public class RectangleComponent extends Component {
    public Rectangle rectangle;
    public RectangleComponent(float x, float y, float width, float height) {
        this.rectangle = new Rectangle(x, y, width, height);
    }
}