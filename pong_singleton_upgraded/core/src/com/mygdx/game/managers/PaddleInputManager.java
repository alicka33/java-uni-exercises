package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.components.RectangleComponent;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.globals.GameConstants;

public class PaddleInputManager {
    private Entity paddleEntity;
    private int upKey;
    private int downKey;
    private int paddleSpeed;

    public PaddleInputManager(Entity paddleEntity, int upKey, int downKey, int paddleSpeed) {
        this.paddleEntity = paddleEntity;
        this.upKey = upKey;
        this.downKey = downKey;
        this.paddleSpeed = paddleSpeed;
    }

    public void update() {
        Rectangle paddleRectangle = paddleEntity.getComponent(RectangleComponent.class).rectangle;
        if (Gdx.input.isKeyPressed(upKey) && paddleRectangle.y < GameConstants.HEIGHT - GameConstants.PADDLE_HEIGHT) {
            paddleRectangle.y += paddleSpeed;
        }
        if (Gdx.input.isKeyPressed(downKey) && paddleRectangle.y > 0) {
            paddleRectangle.y -= paddleSpeed;
        }
    }
}