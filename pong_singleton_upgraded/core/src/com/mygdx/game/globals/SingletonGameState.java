package com.mygdx.game.globals;

public class SingletonGameState {
    private boolean gameRunning = true;
	private boolean gameRestarting = false;
    public int player1Score = 0;
    public int player2Score = 0;
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
        return  gameRunning;
    }
    public boolean getGameRestarting(){
        return  gameRunning;
    }
    public void setGameRunning(boolean gameRunning){
        this.gameRunning = gameRunning;
    }
    public void setGameRestarting(boolean gameRestarting){
        this.gameRestarting = gameRestarting;
    }

}
