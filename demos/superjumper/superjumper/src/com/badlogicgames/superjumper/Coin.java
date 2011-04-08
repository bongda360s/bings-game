package com.badlogicgames.superjumper;


public class Coin extends DynamicGameObject {
    public static final float COIN_WIDTH = 0.5f;
    public static final float COIN_HEIGHT = 0.8f;
    public static final int COIN_SCORE = 10;
    
    public Coin(float x, float y) {
        super(x, y, COIN_WIDTH, COIN_HEIGHT);
    }
    
    public void update(float deltaTime) {
        stateTime += deltaTime;
    }
}
