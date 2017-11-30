package client.controller;

import client.app.GameState;

/**
 * Created by andreas on 2016-10-28.
 */
public class Controller {
    private GameState gameState;
    public void createApplication(){
        gameState = new GameState();
    }
    public void startApplication(){
//        gameState.start();
    }
}
