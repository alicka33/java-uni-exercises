package com.mygdx.game;

import static com.mygdx.game.GameConstants.GAME_ENDING_POINTS;

public class SingletonGameState {
    private boolean gameRunning = true;
    private boolean gameRestarting = false;
    private int player1Score = 0;
    private int player2Score = 0;
    private static volatile SingletonGameState instance = null;
    private SingletonGameState(){}
    public static SingletonGameState getInstance(){
        if (instance == null) {
            synchronized(SingletonGameState.class) {
                if (instance == null) {
                    instance = new SingletonGameState();
                }
            }
        }
        return instance;
    }

    public boolean getGameRunning(){
        return gameRunning;
    }
    public boolean getGameRestarting(){
        return  gameRestarting;
    }
    public void setGameRunning(boolean gameRunning){
        this.gameRunning = gameRunning;
    }
    public void setGameRestarting(boolean gameRestarting){
        this.gameRestarting = gameRestarting;
    }
    public void incrementPlayer1Score(){
        player1Score++;
    }
    public void incrementPlayer2Score(){
        player2Score++;
    }
    public void resetGame(){
        gameRunning = true;
        gameRestarting = false;
        player1Score = 0;
        player2Score = 0;
    }
    public void checkGameOver() {
        if (player1Score == GAME_ENDING_POINTS || player2Score == GAME_ENDING_POINTS) {
            gameRunning = false;
        }
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public int getPlayer1Score() {
        return player1Score;
    }
}

