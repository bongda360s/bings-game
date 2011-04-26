package com.badlogic.gdxinvaders.simulation;

import com.badlogic.gdx.math.Vector3;

public class Missile {
	public static float MISSILE_VELOCITY = 10;
	public final Vector3 position = new Vector3();
	public boolean isInvaderShot;
	public boolean hasLeftField = false;

	public Missile (Vector3 position, boolean isInvaderShot) {
		this.position.set(position);
		this.isInvaderShot = isInvaderShot;
	}

	public void update (float delta) {
		if (position.z > Simulation.PLAYFIELD_MAX_Z) hasLeftField = true;
		if (position.z < Simulation.PLAYFIELD_MIN_Z) hasLeftField = true;
	}
}
