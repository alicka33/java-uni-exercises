package com.mygdx.game.systems;

import static com.mygdx.game.globals.GameConstants.GAME_ENDING_POINTS;
import static com.mygdx.game.globals.GameConstants.HEIGHT;
import static com.mygdx.game.globals.GameConstants.WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.globals.ModifiedShapeRenderer;
import com.mygdx.game.globals.SingletonGameState;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.observers.EntityChangeObserver;

public class RenderSystem implements EntityChangeObserver {

    private ModifiedShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private SingletonGameState gameState;

    public RenderSystem(){
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ModifiedShapeRenderer();
        gameState = SingletonGameState.getInstance();
    }
    public void clear(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
    }
    public void drawScore(){
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, gameState.player1Score + " | " + gameState.player2Score, WIDTH / 2 - 20, HEIGHT - 20);
        batch.end();
    }

    public void drawGameOver() {
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        font.draw(batch, "Game Over!", WIDTH / 2 - 80, HEIGHT / 2 + 70);
        font.draw(batch, "Player " + (gameState.player1Score == GAME_ENDING_POINTS ? "1" : "2") + " wins!", WIDTH / 2 - 90, HEIGHT / 2 + 20);

        font.getData().setScale(1.5f);
        font.draw(batch, "Score: " + (gameState.player1Score) + " : " + (gameState.player2Score), WIDTH / 2 - 60, HEIGHT / 2 - 40);

        font.getData().setScale(2f);
        font.setColor(Color.BLACK);
        font.draw(batch, "Restart", WIDTH / 2 - 45, HEIGHT / 4 + 30);

        batch.end();
    }

    public void unprojectCamera(Vector3 touchPos){
        camera.unproject(touchPos);
    }
    public void dispose(){
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
    @Override
    public void notifyEntityChange(Entity entity) {
        shapeRenderer.draw(entity);
    }
}