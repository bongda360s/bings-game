package com.badlogic.gdxinvaders.simulation;

import com.badlogic.gdx.math.Vector3;

public class Shield {
	public static final float Shield_LIVE_TIME = 2;
	public float aliveTime = 0;
	public float scaleFactor = 2;
	public Vector3 position = new Vector3();
	public static int count = 3;
	public boolean isShield = false;	
	
	public void update (float delta) {
		scaleFactor += delta;
		aliveTime += delta;
	}
	
	public void reset(){
		aliveTime = 0;
		scaleFactor = 2;
		isShield = false;
	}
}
