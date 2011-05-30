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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdxinvaders.screens.GameLoop;

public class Simulation implements Disposable {
	public final static float PLAYFIELD_MIN_X = -14;
	public final static float PLAYFIELD_MAX_X = 14;
	public final static float PLAYFIELD_MIN_Z = -15;
	public final static float PLAYFIELD_MAX_Z = 2;

	public ArrayList<Invader> invaders = new ArrayList<Invader>();
	public ArrayList<Block> blocks = new ArrayList<Block>();
	public ArrayList<Block> borders = new ArrayList<Block>();
	public ArrayList<Shot> shots = new ArrayList<Shot>();
	public Missile missile = new Missile();
	public ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	public Ship ship;
	public Shield shield = new Shield();
	public ArrayList<Shot> shipShots = new ArrayList<Shot>();
	public transient SimulationListener listener;
	public float multiplier = 1;
	public int score;
	public int wave;
	private ArrayList<Explosion> removedExplosions = new ArrayList<Explosion>();
	private Music[] backgroundMusics = new Music[2];
	private final boolean[][] array = {
		{true,true,true,true,true,true,true,true,true},
		{true,true,true,true,true,true,true,true,true},
		{false,true,true,true,true,true,true,true,false},
		{false,true,true,true,true,true,true,true,false},
		{false,true,true,true,true,true,true,true,false},
		{false,false,true,true,true,true,true,false,false},
		{false,false,true,true,true,true,true,false,false},
		{false,false,false,true,true,true,false,false,false},
		{false,false,false,true,true,true,false,false,false},
		{false,false,false,false,true,false,false,false,false}};
//	private final boolean[][] array = {
//			{false,false,false,false,true,false,false,false,false},
//			{false,false,false,false,true,false,false,false,false},
//			{false,false,false,false,true,false,false,false,false},
//			{false,false,false,false,true,false,false,false,false},
//			{false,false,false,false,true,false,false,false,false},
//			{false,false,false,false,true,false,false,false,false},
//			{false,false,false,false,true,false,false,false,false},
//			{false,false,false,false,true,false,false,false,false},
//			{false,false,false,false,false,false,false,false,false},
//			{false,false,false,false,false,false,false,false,false}};
	private Date loopbegin = new Date();
	private float totalElapse = 0f;
	public int awardScore = 0;
	public int awardShip = 0;
	public int awardMissile = 0;
	public int awardShield = 0;
	public float awardWait = 0f;
	public static float totalFreezingTime = 1f;
	public Simulation () {
		backgroundMusics[0] = Gdx.audio.newMusic(Gdx.files.internal("data/background1.ogg"));
		backgroundMusics[1] = Gdx.audio.newMusic(Gdx.files.internal("data/background2.ogg"));
		borders.add(new Block(new Vector3(-14.8f,0,0)));
		borders.add(new Block(new Vector3(14.8f,0,0)));
		populate();
	}

	private void populate () {
		totalFreezingTime = 1 - 0.05f * wave;
		totalFreezingTime = totalFreezingTime > 0.2f? totalFreezingTime : 0.2f;
		ship = new Ship();
		int weight = wave < 10 ? 10 - wave : 1;		
		int[] nos =  {(int)(Math.random() * weight),(int)(Math.random() * weight),(int)(Math.random() * weight),(int)(Math.random() * weight)};
		Arrays.sort(nos);
		
		boolean[][] invaderArray = {array[nos[0]],array[nos[1]],array[nos[2]],array[nos[3]]};
		
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 8; column++) {
				if(invaderArray[row][column]){
					Invader invader = new Invader(new Vector3(-PLAYFIELD_MAX_X / 2 + column * 2f, 0, PLAYFIELD_MIN_Z + row * 2f + 2));
					invaders.add(invader);
				}
			}
		}
		for (int shield = 0; shield < 3; shield++) {
			blocks.add(new Block(new Vector3(-10 + shield * 10 - 1, 0, -2)));
			blocks.add(new Block(new Vector3(-10 + shield * 10 - 1, 0, -3)));
			blocks.add(new Block(new Vector3(-10 + shield * 10 + 0, 0, -3)));
			blocks.add(new Block(new Vector3(-10 + shield * 10 + 1, 0, -3)));
			blocks.add(new Block(new Vector3(-10 + shield * 10 + 1, 0, -2)));
		}

		wave++;
		backgroundMusics[wave & 1].stop();
		backgroundMusics[1 - wave & 1].setLooping(true);
		backgroundMusics[1 - wave & 1].setVolume(Settings.getMusicVolume());
		backgroundMusics[1 - wave & 1].play();
		Settings.setMusic(backgroundMusics[1 - wave & 1]);
	}

	public void update (float delta) {
		if(Settings.getStatus() == 1){			
			updateShip(delta);
			updateInvaders(delta);
			updateShots(delta);
			updateMissile(delta);
			updateExplosions(delta);
			updateShield(delta);
			checkShipCollision();
			checkInvaderCollision();
			checkBlockCollision();
			checkShieldCollision();
		}
		checkNextLevel();
		if(Settings.getClickNewAd()){
			ship.lives++;
			Settings.setClickNewAd(false);
		}
	}
	
	private void updateShip(float delta){
		ship.update(delta);
	}
	private void updateInvaders (float delta) {
		for (int i = 0; i < invaders.size(); i++) {
			Invader invader = invaders.get(i);
			invader.update(delta, multiplier);
		}
	}

	private void updateShots (float delta) {
		for (int i = shots.size()-1; i >= 0; i--) {
			Shot shot = shots.get(i);
			shot.update(delta);
			if (shot.hasLeftField) shots.remove(i);
		}
		for(int i = shipShots.size()-1; i >= 0; i--){
			Shot shipShot = shipShots.get(i);
			shipShot.update(delta);
			if (shipShot.hasLeftField) shipShots.remove(i);
		}

		if (Math.random() < 0.01 * multiplier * Math.log(wave+1)  && invaders.size() > 0) {
			int index = (int)(Math.random() * (invaders.size() - 1));
			Shot shot = new Shot(invaders.get(index).position, true,(Math.random() - 0.5) * Math.PI /3);
			shots.add(shot);
			if (listener != null) listener.shot();
		}
	}

	private void updateMissile (float delta) {
		if(missile!=null)
			missile.update(delta);		
	}
	
	public void updateExplosions (float delta) {
		removedExplosions.clear();
		for (int i = 0; i < explosions.size(); i++) {
			Explosion explosion = explosions.get(i);
			explosion.update(delta);
			if (explosion.aliveTime > Explosion.EXPLOSION_LIVE_TIME) removedExplosions.add(explosion);
		}

		for (int i = removedExplosions.size()-1; i >= 0; i--)
			explosions.remove(removedExplosions.get(i));
	}

	public void updateShield(float delta){
		if(shield.aliveTime > Shield.Shield_LIVE_TIME){
			shield.reset();
		}else
			shield.update(delta);
	}
	private void checkInvaderCollision () {
		for(int i = invaders.size()-1; i >= 0 ; i--){
			if(invaders.get(i).hasLeftField) invaders.remove(i);
		}
		for(int i = 0; i < shipShots.size(); i++){
			Shot shipShot = shipShots.get(i);
			for (int j = 0; j < invaders.size(); j++) {
				Invader invader = invaders.get(j);
				if (invader.position.dst(shipShot.position) < Invader.INVADER_RADIUS) {
					shipShot.hasLeftField = true;
					invaders.remove(invader);
					explosions.add(new Explosion(invader.position));
					if (listener != null) listener.explosion();
					score += Invader.INVADER_POINTS;
					break;
				}
			}
		}
		if (missile.isLaunch){
			if(missile.targetInvader==null){
				missile.isLaunch = false;
				explosions.add(new Explosion(missile.position));
				if (listener != null) listener.explosion();
			}else if(missile.targetInvader.position.dst(missile.position) < Invader.INVADER_RADIUS + Missile.MISSILE_RADIUS){	
				for (int j = 0; j < invaders.size(); j++) {
						Invader invader = invaders.get(j);
						if (invader.position.dst(missile.position) < Invader.INVADER_RADIUS + Missile.MISSILE_RADIUS + 1.4) {
							invader.hasLeftField = true;
							explosions.add(new Explosion(invader.position));
							score += Invader.INVADER_POINTS;
						}
					}
					missile.targetInvader.hasLeftField = true;
					score += Invader.INVADER_POINTS;					
					explosions.add(new Explosion(missile.position));
					missile.isLaunch = false;
					if (listener != null) listener.explosion();
			}
			
		}
	}
	
	private void checkShipCollision () {
		if (!ship.isExploding && !ship.isInitial) {
			for (int i = 0; i < shots.size(); i++) {
				Shot shot = shots.get(i);
				if (!shot.isInvaderShot) continue;

				if (ship.position.dst(shot.position) < Ship.SHIP_RADIUS * 0.8) {
					Shield.count = 3;
					Missile.count = 3;
					shot.hasLeftField = true;
					ship.lives--;
					ship.initialTime = 3;
					ship.isInitial = true;
					ship.isExploding = true;
					explosions.add(new Explosion(ship.position));
					if (listener != null) listener.explosion();
					break;
				}
			}
			for (int i = 0; i < invaders.size(); i++) {
				Invader invader = invaders.get(i);
				if (invader.position.dst(ship.position) < Ship.SHIP_RADIUS) {
					Shield.count = 3;
					Missile.count = 3;
					ship.lives--;
					ship.initialTime = 3;
					ship.isInitial = true;
					invaders.remove(invader);
					ship.isExploding = true;
					explosions.add(new Explosion(invader.position));
					explosions.add(new Explosion(ship.position));
					if (listener != null) listener.explosion();
					break;
				}
			}
		}
	}
	
	private void checkShieldCollision(){
		if(!shield.isShield) return;
		for (int i = 0; i < shots.size(); i++) {
			Shot shot = shots.get(i);
			if (shield.position.dst(shot.position) < Ship.SHIP_RADIUS * shield.scaleFactor) {
				shot.hasLeftField = true;
			}
		}
	}
	
	private void checkBlockCollision () {
		for (int i = 0; i < shots.size(); i++) {
			Shot shot = shots.get(i);

			for (int j = 0; j < blocks.size(); j++) {
				Block block = blocks.get(j);
				if (block.position.dst(shot.position) < Block.BLOCK_RADIUS) {
					shot.hasLeftField = true;
					blocks.remove(block);
					break;
				}
			}
		}
		
		for (int i = 0; i < shipShots.size(); i++) {
			Shot shipShot = shipShots.get(i);

			for (int j = 0; j < blocks.size(); j++) {
				Block block = blocks.get(j);
				if (block.position.dst(shipShot.position) < Block.BLOCK_RADIUS) {
					shipShot.hasLeftField = true;
					blocks.remove(block);
					break;
				}
			}
		}
	}

	private void checkNextLevel () {
		if(Settings.getStatus() == 1){
			awardWait = 0;
			totalElapse += Gdx.graphics.getDeltaTime();
			if (invaders.size() <= 0 && ship.lives > 0) {			
				blocks.clear();
				shots.clear();
				shipShots.clear();
				int lives = ship.lives;
				populate();
				ship.lives = lives;
				multiplier += 0.1f;	
				awardScore = (int)(wave * 2000 * (Math.PI/2 - Math.atan(totalElapse/30)));
				awardShip = awardScore > 1000 * wave? 1 : 0;
				ship.lives += awardShip;
				score += awardScore;
				awardMissile = awardScore > 500 * wave? 1:0;
				Missile.count += awardMissile;
				awardShield = awardScore > 300 * wave? 1:0;
				Shield.count += awardShield;
				Settings.setStatus(2); //award				
			}
		}
		else if(Settings.getStatus() == 2){	
			totalElapse = 0;
			awardWait += Gdx.graphics.getDeltaTime();
		}
	}

	public void moveShipLeft (float delta, float scale) {
		if (ship.isExploding) return;

		ship.position.x -= delta * Ship.SHIP_VELOCITY * scale;
		if (ship.position.x < PLAYFIELD_MIN_X) ship.position.x = PLAYFIELD_MIN_X;
	}

	public void moveShipRight (float delta, float scale) {
		if (ship.isExploding) return;
		ship.position.x += delta * Ship.SHIP_VELOCITY * scale;
		if (ship.position.x > PLAYFIELD_MAX_X) ship.position.x = PLAYFIELD_MAX_X;
	}

	public void shot () {
			if (!ship.isExploding && ship.canShot) {
				Shot shipShot = new Shot(ship.position, false, Math.atan(ship.position.x/40));
				shipShots.add(shipShot);
				if (listener != null) listener.shot();
				ship.canShot = false;
			}
	}
	
	public void shield(){
		if(!shield.isShield && shield.count > 0 && !ship.isExploding ){
			ship.isInitial = true;
			ship.initialTime = Shield.Shield_LIVE_TIME;
			shield.position = ship.position;
			shield.isShield = true;
			shield.count--;
			if (listener != null) listener.shield();
		}
	}
	public void launch(){
		if(!missile.isLaunch && Missile.count > 0){
			Missile.count--;
			missile.position.set((float)(ship.position.x + Math.random()-0.5),ship.position.y-0.5f,0);
			float minDist = Float.MAX_VALUE;
			for (int i = 0; i < invaders.size(); i++) {
				Invader invader = invaders.get(i);
				float tempDist = missile.position.dst(invader.position);
				if(tempDist < minDist){
					minDist = tempDist;
					missile.targetInvader = invader;
				}
			}			
			missile.isLaunch = true;
			missile.update(0);
			if (listener != null) listener.launch();
		}
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		backgroundMusics[0].dispose();
		backgroundMusics[1].dispose();
	}
	
	public boolean isGameOver(){
		boolean isInvaderPassed = false;
		for (int i = 0; i < invaders.size(); i++) {
			Invader invader = invaders.get(i);
			if(invader.position.z > 1){
				isInvaderPassed = true;
				break;
			}
		}
		return ship.lives <= 0 || isInvaderPassed;
	}
	
}
