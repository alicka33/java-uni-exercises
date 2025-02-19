package com.mygdx.game.managers;

import static com.mygdx.game.globals.GameConstants.BALL_SIZE;
import static com.mygdx.game.globals.GameConstants.BALL_STARTING_SPEED;
import static com.mygdx.game.globals.GameConstants.HEIGHT;
import static com.mygdx.game.globals.GameConstants.PADDLE_HEIGHT;
import static com.mygdx.game.globals.GameConstants.PADDLE_WIDTH;
import static com.mygdx.game.globals.GameConstants.RESTART_BUTTON_HEIGHT;
import static com.mygdx.game.globals.GameConstants.RESTART_BUTTON_WIDTH;
import static com.mygdx.game.globals.GameConstants.WIDTH;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.components.RectangleComponent;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.components.ShapeComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.enums.EntityType;
import com.mygdx.game.enums.Shape;
import com.mygdx.game.globals.GameConstants;
import com.mygdx.game.globals.SingletonGameState;
import com.mygdx.game.observers.EntityChangeObserver;
import com.mygdx.game.observers.GameOverObserver;
import com.mygdx.game.observers.RestartObserver;
import com.mygdx.game.systems.RenderSystem;

import java.util.ArrayList;
import java.util.List;

public class EntityManager implements GameOverObserver, RestartObserver {
	private List<Entity> gameEntities = new ArrayList<>();
	private List<Entity> restartEntities = new ArrayList<>();
	private List<EntityChangeObserver> entityChangeObservers = new ArrayList<>();
	private PaddleInputManager paddle1InputManager;
	private PaddleInputManager paddle2InputManager;
	private SingletonGameState gameState;
	private RenderSystem renderSystem;
	private int consecutivePassCounter = 0;
	public EntityManager(RenderSystem renderSystem){
		gameState = SingletonGameState.getInstance();
		this.renderSystem = renderSystem;
	}

	public void addEntityChangeObserver(EntityChangeObserver observer){
		entityChangeObservers.add(observer);
	}
	public void createEntities(){
		createPaddle(1, 10, HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);
		createPaddle(2, WIDTH - PADDLE_WIDTH - 10, HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);
		createBall(WIDTH / 2 - BALL_SIZE / 2, HEIGHT / 2 - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE, BALL_STARTING_SPEED);
		createRestartButton(WIDTH / 2 - RESTART_BUTTON_WIDTH / 2, HEIGHT / 4, RESTART_BUTTON_WIDTH, RESTART_BUTTON_HEIGHT);
	}
	private void createPaddle(int paddleNumber, float x, float y, float width, float height) {
		Entity entity = new Entity("Paddle" + paddleNumber, EntityType.interactive);
		entity.add(new RectangleComponent(x, y, width, height));
		entity.add(new VelocityComponent(0, 0));
		entity.add(new ShapeComponent(Shape.rect, Color.WHITE));
		gameEntities.add(entity);
		if(paddleNumber == 1){
			paddle1InputManager = new PaddleInputManager(entity, Input.Keys.W, Input.Keys.S, GameConstants.PADDLE_SPEED);
		}else{
			paddle2InputManager = new PaddleInputManager(entity, Input.Keys.UP, Input.Keys.DOWN, GameConstants.PADDLE_SPEED);
		}
	}

	private void createBall(float x, float y, float width, float height, int speed) {
		Entity entity = new Entity("Ball", EntityType.still);
		entity.add(new RectangleComponent(x, y, width, height));
		entity.add(new VelocityComponent(MathUtils.randomSign() * speed, MathUtils.randomSign() * speed));
		entity.add(new ShapeComponent(Shape.circle, Color.WHITE));
		gameEntities.add(entity);
	}

	private void createRestartButton(float x, float y, float width, float height) {
		Entity entity = new Entity("Restart Button", EntityType.interactive);
		entity.add(new RectangleComponent(x, y, width, height));
		entity.add(new ShapeComponent(Shape.rect, Color.GREEN));
		restartEntities.add(entity);
	}

	public void update() {
		renderSystem.clear();
		for (Entity entity : gameEntities) {
			for (EntityChangeObserver entityChangeObserver : entityChangeObservers){
				entityChangeObserver.notifyEntityChange(entity);
			}
			if(entity.getName().equals("Ball")){
				checkBallOverlapping(entity);
			}
		}
		paddle1InputManager.update();
		paddle2InputManager.update();
		renderSystem.drawScore();
	}

	public void checkBallOverlapping(Entity ball){
		Rectangle ballRectangle = ball.getComponent(RectangleComponent.class).rectangle;
		VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class);

		for (Entity entity : gameEntities) {
			if(entity == ball) continue;
			Rectangle entityRectangle = entity.getComponent(RectangleComponent.class).rectangle;
			if (ballRectangle.overlaps(entityRectangle)) {
				ballVelocity.x = -ballVelocity.x;
				consecutivePassCounter++;
			}
		}

		if (ballRectangle.x < 0) {
			gameState.player2Score++;
			resetGameEntities();
		} else if (ballRectangle.x > WIDTH - BALL_SIZE) {
			gameState.player1Score++;
			resetGameEntities();
		}

		if (ballRectangle.y < 0 || ballRectangle.y > HEIGHT - BALL_SIZE) {
			ballVelocity.y = -ballVelocity.y;
		}

		if (consecutivePassCounter >= GameConstants.CONSECUTIVE_PASS_THRESHOLD) {
			ballVelocity.x *= 1.5;
			ballVelocity.y *= 1.5;
			consecutivePassCounter = 0;
		}
	}

	private void resetGameEntities() {
		Entity ball = null;

		for(Entity entity : gameEntities){
			if(entity.getName().equals("Ball")){
				ball = entity;
			}
			else if(entity.getName().contains("Paddle")) {
				Rectangle paddleRectangle = entity.getComponent(RectangleComponent.class).rectangle;
				paddleRectangle.y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
			}
		}

		VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class);
		Rectangle ballRectangle = ball.getComponent(RectangleComponent.class).rectangle;

		if (!gameState.getGameRunning()) {
			gameState.setGameRestarting(true);
		}

		consecutivePassCounter = 0;
		ballVelocity.set(MathUtils.randomSign() * BALL_STARTING_SPEED, MathUtils.randomSign() * BALL_STARTING_SPEED);

		ballRectangle.x = WIDTH / 2 - BALL_SIZE / 2;
		ballRectangle.y = HEIGHT / 2 - BALL_SIZE / 2;

		if (gameState.getGameRestarting()) {
			gameState.setGameRestarting(false);
		}
	}
	public boolean checkIfRestart(Vector3 touchPos){
		renderSystem.unprojectCamera(touchPos);
		Rectangle restartButtonPosition = getEntityByName("Restart Button").getComponent(RectangleComponent.class).rectangle;
		if (restartButtonPosition.contains(touchPos.x, touchPos.y)) {
			return true;
		}
		return false;
	}
	public void updateEntityChanges(){
		for (Entity entity : restartEntities){
			renderSystem.notifyEntityChange(entity);
		}
	}
	@Override
	public void notifyGameOver() {
		renderSystem.clear();
		updateEntityChanges();
		renderSystem.drawGameOver();
	}
	@Override
	public void notifyRestart() {
		resetGameEntities();
	}

	private Entity getEntityByName(String name){
		for(Entity entity : gameEntities){
			if(entity.getName().equals(name)){
				return entity;
			}
		}
		for(Entity entity : restartEntities){
			if(entity.getName().equals(name)){
				return entity;
			}
		}
		return null;
	}
}