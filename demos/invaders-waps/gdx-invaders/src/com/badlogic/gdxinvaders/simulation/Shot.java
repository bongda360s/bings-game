/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdxinvaders.simulation;

import com.badlogic.gdx.math.Vector3;

public class Shot {
	public static float SHOT_VELOCITY = 10;
	public final Vector3 position = new Vector3();
	public boolean isInvaderShot;
	public boolean hasLeftField = false;
	public double angle = 0;

	public Shot (Vector3 position, boolean isInvaderShot, double angle) {
		this.position.set(position);
		this.isInvaderShot = isInvaderShot;
		this.angle = angle;
	}

	public void update (float delta) {
		if (isInvaderShot){
			position.z += SHOT_VELOCITY * delta * Math.cos(angle);
			position.x += SHOT_VELOCITY * delta * Math.sin(angle);
		}
		else{
			position.z -= SHOT_VELOCITY * delta * Math.cos(angle);
			position.x -= SHOT_VELOCITY * delta * Math.sin(angle);
		}	
		if (position.z > Simulation.PLAYFIELD_MAX_Z) hasLeftField = true;
		if (position.z < Simulation.PLAYFIELD_MIN_Z) hasLeftField = true;
	}
}
