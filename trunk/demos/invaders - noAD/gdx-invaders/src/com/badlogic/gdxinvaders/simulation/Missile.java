package com.badlogic.gdxinvaders.simulation;

import com.badlogic.gdx.math.Vector3;

public class Missile {
	public static float MISSILE_RADIUS = 0.6f;
	public static float MISSILE_VELOCITY = 20;
	public final Vector3 position = new Vector3();
	public final Vector3 direction = new Vector3(0,0,-1);
	//public boolean isInvaderMissile;
	public boolean hasLeftField = false;
	public boolean isLaunch = false;
	public float flyTime = 0;
	public Invader targetInvader;	
	public static int count = 3;

	public void update (float delta) {
		if(targetInvader!=null && isLaunch){
			flyTime += delta;
			direction.set(targetInvader.position.x - position.x,targetInvader.position.y - position.y,targetInvader.position.z - position.z);
			direction.mul(1/direction.len());
			float factor = (float)(MISSILE_VELOCITY * delta * Math.atan(flyTime * 5) * 2 / Math.PI);
			position.add( factor * direction.x,factor * direction.y,factor * direction.z);
		}
	}
}
