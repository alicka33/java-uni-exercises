package com.mygdx.game.managers;

import static com.mygdx.game.globals.GameConstants.GAME_ENDING_POINTS;

import com.mygdx.game.globals.SingletonGameState;
import com.mygdx.game.observers.GameOverObserver;
import com.mygdx.game.observers.RestartObserver;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
	private List<GameOverObserver> gameOverObservers = new ArrayList<>();
	private List<RestartObserver> restartObservers = new ArrayList<>();
	private SingletonGameState gameState;
	public GameManager(){
		gameState = SingletonGameState.getInstance();
	}
	public void addGameOverObserver(GameOverObserver observer) {
		gameOverObservers.add(observer);
	}
	public void addRestartObserver(RestartObserver observer) {
		restartObservers.add(observer);
	}
	public void gameOver() {
		for (GameOverObserver observer : gameOverObservers) {
			observer.notifyGameOver();
		}
	}
	public void restartGame() {
		gameState.setGameRunning(true);
		gameState.player1Score = 0;
		gameState.player2Score = 0;
		for (RestartObserver observer : restartObservers) {
			observer.notifyRestart();
		}
	}
	public void checkGameOver() {
		if (gameState.player1Score == GAME_ENDING_POINTS || gameState.player2Score == GAME_ENDING_POINTS) {
			gameState.setGameRunning(false);
			gameOver();
		}
	}


}