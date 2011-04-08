package com.badlogicgames.superjumper;

import com.badlogic.gdx.math.Vector2;

public abstract class DynamicGameObject extends GameObject {
    public final Vector2 velocity;
    public final Vector2 accel;
    public float stateTime;
    
    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        velocity = new Vector2();
        accel = new Vector2();
        stateTime = 0;
    }
    
    public abstract void update(float deltaTime);
}
