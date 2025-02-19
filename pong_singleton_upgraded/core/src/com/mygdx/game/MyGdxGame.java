package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.globals.SingletonGameState;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameManager;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.RenderSystem;

public class MyGdxGame extends ApplicationAdapter {
	private EntityManager entityManager;
	private GameManager gameManager;
	private RenderSystem renderSystem;
	private MovementSystem movementSystem;
	private SingletonGameState gameState;

	@Override
	public void create() {
		renderSystem = new RenderSystem();
		movementSystem = new MovementSystem();
		entityManager = new EntityManager(renderSystem);
		gameManager = new GameManager();
		gameState = SingletonGameState.getInstance();

		entityManager.addEntityChangeObserver(renderSystem);
		entityManager.addEntityChangeObserver(movementSystem);

		gameManager.addRestartObserver(entityManager);
		gameManager.addGameOverObserver(entityManager);

		entityManager.createEntities();
		gameState.setGameRunning(true);
	}

	@Override
	public void render() {
		if (gameState.getGameRunning()) {
			entityManager.update();
			gameManager.checkGameOver();
		} else {
			gameManager.gameOver();
			checkForRestart();
		}
	}
	public void checkForRestart() {
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			if(entityManager.checkIfRestart(touchPos)){
				gameManager.restartGame();
			}
		}
	}
	@Override
	public void dispose() {
		renderSystem.dispose();
	}
}







