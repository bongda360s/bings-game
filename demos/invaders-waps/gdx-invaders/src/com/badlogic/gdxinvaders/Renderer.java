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

package com.badlogic.gdxinvaders;

import java.io.InputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureDict;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdxinvaders.simulation.Block;
import com.badlogic.gdxinvaders.simulation.Explosion;
import com.badlogic.gdxinvaders.simulation.Invader;
import com.badlogic.gdxinvaders.simulation.Missile;
import com.badlogic.gdxinvaders.simulation.Settings;
import com.badlogic.gdxinvaders.simulation.Shield;
import com.badlogic.gdxinvaders.simulation.Ship;
import com.badlogic.gdxinvaders.simulation.Shot;
import com.badlogic.gdxinvaders.simulation.Simulation;

/**
 * The renderer receives a simulation and renders it.
 * @author mzechner
 * 
 */
public class Renderer {
	/** sprite batch to draw text **/
	private final SpriteBatch spriteBatch;
	
	/** the rotation angle of all invaders around y **/
	private float invaderAngle = 0;
	/** status string **/
	private String status = "";
	
	/** view and transform matrix for text rendering **/
	private Matrix4 viewMatrix = new Matrix4();
	private Matrix4 transformMatrix = new Matrix4();

	/** perspective camera **/
	private PerspectiveCamera camera;
	
	private static Texture earth;
	/** the font **/
	private static BitmapFont font;
	/** the background texture **/
	private static Texture background;
	private static Texture title;
	/** the ship mesh **/
	private static Mesh shipMesh;
	/** the ship texture **/
	private static Texture shipTexture;
	/** the invader mesh **/
	private static Mesh invaderMesh;
	/** the invader texture **/
	private static Texture invaderTexture;
	//private Texture shieldTexture;
	private static Mesh shieldMesh;
	/** the block mesh **/
	private static Mesh blockMesh;
	/** the shot mesh **/
	private static Mesh shotMesh;
	private static Texture missileTexture;
	private static Mesh missileMesh;
	/** the explosion mesh **/
	private static Mesh explosionMesh;
	/** the explosion texture **/
	private static Texture explosionTexture;
	private static TextureRegion planeDemo;
	private static TextureRegion level;
	private static TextureRegion award;
	private static TextureRegion missileDemo;
	private static TextureRegion shieldDemo;
	private static TextureRegion levelComplete;
	private static TextureRegion continueRegion;
	private static boolean isLoaded = false;
	//private Texture playing;
	public Renderer (Application app) {	
		spriteBatch = new SpriteBatch();
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public static void loadAssests(){
		if(!isLoaded){
			font = new BitmapFont();
			font.setColor(1, 1, 0, 1);
			earth = TextureDict.loadTexture("data/earth.png").get();
			earth.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			background = TextureDict.loadTexture("data/background.png").get();
			background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			title = TextureDict.loadTexture("data/title.png").get();
			title.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			levelComplete = new TextureRegion(title,0,300,420,78);
			planeDemo = new TextureRegion(title,512-64,0,64,60);
			level = new TextureRegion(title,0,512 - 135,64,64);
			award = new TextureRegion(title,512-64,64,64,64);
			missileDemo = new TextureRegion(title,512-64,128,64,64);
			shieldDemo = new TextureRegion(title,512-64,512-64,64,64);
			continueRegion = new TextureRegion(title,98,385,204,28);
			/*
			planeDemo = TextureDict.loadTexture("data/planedemo.png").get();
			planeDemo.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			level = TextureDict.loadTexture("data/medal.png").get();
			level.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			award = TextureDict.loadTexture("data/score.png").get();
			award.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			*/
			try{
				InputStream in = Gdx.files.internal("data/ship.obj").read();
				shipMesh = ModelLoader.loadObj(in);
				in.close();
		
				in = Gdx.files.internal("data/invader.obj").read();
				invaderMesh = ModelLoader.loadObj(in);
				in.close();
		
				in = Gdx.files.internal("data/block.obj").read();
				blockMesh = ModelLoader.loadObj(in);
				in.close();
		
				in = Gdx.files.internal("data/shot.obj").read();
				shotMesh = ModelLoader.loadObj(in);
				in.close();
				
				in = Gdx.files.internal("data/shield.obj").read();
				shieldMesh = ModelLoader.loadObj(in);
				in.close();
				
				in = Gdx.files.internal("data/missile.obj").read();
				missileMesh = ModelLoader.loadObj(in);
				in.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				missileTexture = new Texture(Gdx.files.internal("data/missile.png"), true);
				missileTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
				shipTexture = new Texture(Gdx.files.internal("data/ship.png"), true);
				shipTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);				
				//shieldTexture = new Texture(Gdx.files.internal("data/thunder.png"), true);
				//shieldTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
				invaderTexture = new Texture(Gdx.files.internal("data/invader.png"), true);
				invaderTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
				explosionTexture = new Texture(Gdx.files.internal("data/explode.png"), true);
				explosionTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
				explosionMesh = new Mesh(true, 4 * 16, 0, new VertexAttribute(Usage.Position, 3, "a_position"),
						new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord"));
	
				float[] vertices = new float[4 * 16 * (3 + 2)];
				int idx = 0;
				for (int row = 0; row < 4; row++) {
					for (int column = 0; column < 4; column++) {
						vertices[idx++] = 1;
						vertices[idx++] = 1;
						vertices[idx++] = 0;
						vertices[idx++] = 0.25f + column * 0.25f;
						vertices[idx++] = 0 + row * 0.25f;
	
						vertices[idx++] = -1;
						vertices[idx++] = 1;
						vertices[idx++] = 0;
						vertices[idx++] = 0 + column * 0.25f;
						vertices[idx++] = 0 + row * 0.25f;
	
						vertices[idx++] = -1;
						vertices[idx++] = -1;
						vertices[idx++] = 0;
						vertices[idx++] = 0f + column * 0.25f;
						vertices[idx++] = 0.25f + row * 0.25f;
	
						vertices[idx++] = 1;
						vertices[idx++] = -1;
						vertices[idx++] = 0;
						vertices[idx++] = 0.25f + column * 0.25f;
						vertices[idx++] = 0.25f + row * 0.25f;
					}
				}
			explosionMesh.setVertices(vertices);
			isLoaded = true;
		}
	}
	
	public void render (Application app, Simulation simulation) {
		GL10 gl = app.getGraphics().getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, app.getGraphics().getWidth(), app.getGraphics().getHeight());

		renderBackground(gl,simulation);
		if(Settings.getStatus() == 2){
			renderAward(gl,simulation);
		}else if(Settings.getStatus()==0){
			renderPlay();
		}
		
		gl.glDisable(GL10.GL_DITHER);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_CULL_FACE);

		setProjectionAndCamera(app.getGraphics(), simulation.ship, app);
		setLighting(gl);

		gl.glEnable(GL10.GL_TEXTURE_2D);

		renderShip(gl, simulation.ship, app);
		renderMissile(gl, simulation.missile, app);
		if(Settings.getStatus() != 2){
			renderInvaders(gl, simulation.invaders);
			renderBorder(gl,simulation.borders);
			gl.glDisable(GL10.GL_TEXTURE_2D);
			renderBlocks(gl, simulation.blocks);
			renderShield(gl,simulation.shield);
			gl.glDisable(GL10.GL_LIGHTING);
			renderShots(gl, simulation.shots);
			renderShots(gl, simulation.shipShots);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			renderExplosions(gl, simulation.explosions);			
			invaderAngle += app.getGraphics().getDeltaTime() * 90;
			if (invaderAngle > 360) invaderAngle -= 360;			
		}
		
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_DEPTH_TEST);
	}

	private void renderBackground (GL10 gl, Simulation simulation) {
		viewMatrix.setToOrtho2D(0, 0, 480, 320);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(background, 0, 0, 480, 320, 0, 0, 1024, 729, false, false);
		spriteBatch.enableBlending();
		if(Settings.getStatus() != 2)
			spriteBatch.draw(earth, 60 - 2 * simulation.ship.position.x, 40, 360, 240, 0, 0, 512, 512, false, false);
		
		spriteBatch.draw(level, 0, Settings.matricHeight - 16, 16, 16);
		font.draw(spriteBatch, "\327 " + simulation.wave, 16, Settings.matricHeight + 4);
		spriteBatch.draw(planeDemo, 0, Settings.matricHeight - 36, 16, 16);
		font.draw(spriteBatch, "\327 " + simulation.ship.lives, 16, Settings.matricHeight - 16);
		spriteBatch.draw(missileDemo, 0, Settings.matricHeight - 56, 16, 16);
		font.draw(spriteBatch, "\327 " + Missile.count, 16, Settings.matricHeight - 36);
		spriteBatch.draw(shieldDemo, 0, Settings.matricHeight - 76, 16, 16);
		font.draw(spriteBatch, "\327 " + Shield.count, 16, Settings.matricHeight - 56);
		spriteBatch.draw(award, 0, Settings.matricHeight - 98, 16, 16);
		font.draw(spriteBatch, "\327 " + Integer.toString(simulation.score), 16, Settings.matricHeight - 76);
		spriteBatch.end();
	}
	
	private void renderAward(GL10 gl, Simulation simulation){
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(levelComplete, 145, 200, 200, 40);
		spriteBatch.draw(award, Settings.matricWidth/2 - 32, Settings.matricHeight/2, 32, 32);
		spriteBatch.draw(planeDemo, Settings.matricWidth/2 - 32, Settings.matricHeight/2 - 40, 32, 32);
		spriteBatch.draw(missileDemo, Settings.matricWidth/2 - 32, Settings.matricHeight/2 - 80, 32, 32);
		spriteBatch.draw(shieldDemo, Settings.matricWidth/2 - 32, Settings.matricHeight/2 - 120, 32, 32);
		font.draw(spriteBatch, " \327 " + simulation.awardScore, Settings.matricWidth/2, Settings.matricHeight/2 + 32);
		font.draw(spriteBatch, " \327 " + simulation.awardShip, Settings.matricWidth/2, Settings.matricHeight/2 - 8);
		font.draw(spriteBatch, " \327 " + simulation.awardMissile, Settings.matricWidth/2, Settings.matricHeight/2 - 48);
		font.draw(spriteBatch, " \327 " + simulation.awardShield, Settings.matricWidth/2, Settings.matricHeight/2 - 88);
		spriteBatch.end();
		if(simulation.awardWait > 1){
			renderPlay();
		}
	}
	
	private void renderPlay(){
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.draw(continueRegion, 180, 257,120,20);
		spriteBatch.end();
	}
	
	final Vector3 dir = new Vector3();

	private void setProjectionAndCamera (Graphics graphics, Ship ship, Application app) {
		camera.position.set(ship.position.x, 6, 2);
		camera.direction.set(ship.position.x, 0, -4).sub(camera.position).nor();		
		camera.update();
		camera.apply(Gdx.gl10);		
	}

	float[] direction = {1, 0.5f, 0, 0};

	private void setLighting (GL10 gl) {
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, direction, 0);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
	}

	private void renderInvaders (GL10 gl, ArrayList<Invader> invaders) {
		invaderTexture.bind();
		for (int i = 0; i < invaders.size(); i++) {
			Invader invader = invaders.get(i);
			gl.glPushMatrix();
			gl.glTranslatef(invader.position.x, invader.position.y, invader.position.z);
			gl.glRotatef(invaderAngle, 0, 1, 0);
			invaderMesh.render(GL10.GL_TRIANGLES);
			gl.glPopMatrix();
		}
	}
	
	private void renderShip (GL10 gl, Ship ship, Application app) {
		if (ship.isExploding) return;
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);
		shipTexture.bind();
		gl.glColor4f(1f,1f, 1f, 1 - 0.3f * ship.initialTime);
		gl.glPushMatrix();
		gl.glTranslatef(ship.position.x, ship.position.y, ship.position.z);
		gl.glRotatef(45 * (-app.getInput().getAccelerometerY() / 5), 0, 0, 1);
		gl.glRotatef(180, 0, 1, 0);
		shipMesh.render(GL10.GL_TRIANGLES);
		gl.glPopMatrix();
		gl.glColor4f(1, 1, 1, 1);
		gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderMissile (GL10 gl, Missile missile, Application app) {
		if(!missile.isLaunch) return;
		setMissileLighting(gl,missile);
		missileTexture.bind();
		gl.glPushMatrix();		
		gl.glTranslatef(missile.position.x, missile.position.y, missile.position.z);
		
		gl.glRotatef(90, -1, 0, 0);
		float angle = (float)(Math.atan(missile.direction.x/missile.direction.z) * 180/Math.PI);
		gl.glRotatef((float)(angle * Math.atan(missile.flyTime * 5) * 2 / Math.PI),0,0,1);
		missileMesh.render(GL10.GL_TRIANGLES);
		gl.glPopMatrix();
	}
	private void renderBorder(GL10 gl, ArrayList<Block> blocks ){
		missileTexture.bind();
		gl.glPushMatrix();
		for(int i = 0; i < blocks.size(); i++){
			Block block = blocks.get(i);
			gl.glPushMatrix();
			gl.glTranslatef(block.position.x, block.position.y, block.position.z);
			gl.glRotatef(90, 0, 0, 1);
			blockMesh.render(GL10.GL_TRIANGLES);
			gl.glPopMatrix();
		}
	}
	
	private void setMissileLighting (GL10 gl, Missile missile) {
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT4);
		gl.glLightfv(GL10.GL_LIGHT4, GL10.GL_POSITION, new float[]{missile.position.x,missile.position.y,missile.position.z + 0.5f,0}, 0);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
	}
	
	private void renderShield (GL10 gl, Shield shield) {
		if(shield.isShield){
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);
			//gl.glBlendFunc(GL10.GL_ONE,GL10.GL_ONE);
			//shieldTexture.bind();		
			gl.glColor4f(0.8f,0.2f, 0.2f, 0.5f);
			//gl.glDepthMask(false);
			gl.glPushMatrix();
			gl.glTranslatef(shield.position.x, shield.position.y, shield.position.z);
			gl.glScalef(shield.scaleFactor, shield.scaleFactor, shield.scaleFactor);
			shieldMesh.render(GL10.GL_TRIANGLES);
			gl.glPopMatrix();
			//gl.glDepthMask(true);
			gl.glColor4f(1, 1, 1, 1);
			gl.glDisable(GL10.GL_BLEND);
		}
	}
	private void renderBlocks (GL10 gl, ArrayList<Block> blocks) {
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(0.2f, 0.2f, 1, 0.7f);
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			gl.glPushMatrix();
			gl.glTranslatef(block.position.x, block.position.y, block.position.z);
			blockMesh.render(GL10.GL_TRIANGLES);
			gl.glPopMatrix();
		}
		gl.glColor4f(1, 1, 1, 1);
		gl.glDisable(GL10.GL_BLEND);
	}

	private void renderShots (GL10 gl, ArrayList<Shot> shots) {
		gl.glColor4f(1, 1, 0, 1);
		for (int i = 0; i < shots.size(); i++) {
			Shot shot = shots.get(i);
			gl.glPushMatrix();
			gl.glTranslatef(shot.position.x, shot.position.y, shot.position.z);
			shotMesh.render(GL10.GL_TRIANGLES);
			gl.glPopMatrix();
		}
		gl.glColor4f(1, 1, 1, 1);
	}

	private void renderExplosions (GL10 gl, ArrayList<Explosion> explosions) {
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);;
		explosionTexture.bind();
		for (int i = 0; i < explosions.size(); i++) {
			Explosion explosion = explosions.get(i);
			gl.glPushMatrix();
			gl.glTranslatef(explosion.position.x, explosion.position.y, explosion.position.z);
			explosionMesh.render(GL10.GL_TRIANGLE_FAN, (int)((explosion.aliveTime / Explosion.EXPLOSION_LIVE_TIME) * 15) * 4, 4);
			gl.glPopMatrix();
		}
		gl.glDisable(GL10.GL_BLEND);
	}
	

	public void dispose () {
		spriteBatch.dispose();		
	}
	public static void unloadAssests(){
		if(isLoaded){
			shipTexture.dispose();
			invaderTexture.dispose();
			explosionTexture.dispose();
			font.dispose();
			explosionMesh.dispose();
			shipMesh.dispose();
			invaderMesh.dispose();
			shotMesh.dispose();
			shieldMesh.dispose();
			blockMesh.dispose();
		}
	}
	
}
