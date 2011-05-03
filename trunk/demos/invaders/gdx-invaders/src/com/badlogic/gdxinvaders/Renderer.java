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
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdxinvaders.simulation.Block;
import com.badlogic.gdxinvaders.simulation.Explosion;
import com.badlogic.gdxinvaders.simulation.Invader;
import com.badlogic.gdxinvaders.simulation.Missile;
import com.badlogic.gdxinvaders.simulation.Settings;
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
	
	private Texture earth;
	/** the font **/
	private BitmapFont font;
	/** the background texture **/
	private Texture background;
	/** the ship mesh **/
	private Mesh shipMesh;
	/** the ship texture **/
	private Texture shipTexture;
	/** the invader mesh **/
	private Mesh invaderMesh;
	/** the invader texture **/
	private Texture invaderTexture;
	private Texture shieldTexture;
	private Mesh shieldMesh;
	/** the block mesh **/
	private Mesh blockMesh;
	/** the shot mesh **/
	private Mesh shotMesh;
	/** the explosion mesh **/
	private Mesh explosionMesh;
	/** the explosion texture **/
	private Texture explosionTexture;
	private Texture planeDemo;
	private Texture level;
	private Texture award;
	private Texture playing;
	public Renderer (Application app) {	
		spriteBatch = new SpriteBatch();
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		font = new BitmapFont();
		font.setColor(1, 1, 0, 1);
		earth = TextureDict.loadTexture("data/earth.png").get();
		earth.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = TextureDict.loadTexture("data/background.png").get();
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		planeDemo = TextureDict.loadTexture("data/planedemo.png").get();
		planeDemo.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		level = TextureDict.loadTexture("data/medal.png").get();
		level.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		award = TextureDict.loadTexture("data/score.png").get();
		award.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		playing = TextureDict.loadTexture("data/play.png").get();
//		playing.setFilter(TextureFilter.Linear, TextureFilter.Linear);
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
			}catch(Exception e){
				e.printStackTrace();
			}
			shipTexture = new Texture(Gdx.files.internal("data/ship.png"), true);
			shipTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);				
			shieldTexture = new Texture(Gdx.files.internal("data/shield.png"), true);
			shieldTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
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
		if(Settings.getStatus() != 2){
			renderInvaders(gl, simulation.invaders);			
			
			gl.glDisable(GL10.GL_TEXTURE_2D);
			renderBlocks(gl, simulation.blocks);
	
			gl.glDisable(GL10.GL_LIGHTING);
			renderShots(gl, simulation.shots);
			
			gl.glEnable(GL10.GL_TEXTURE_2D);
			renderExplosions(gl, simulation.explosions);			
			renderShield(gl,simulation.ship);
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
		spriteBatch.draw(earth, 60 - simulation.ship.position.x, 40 - Gdx.input.getAccelerometerX(), 360, 240, 0, 0, 512, 512, false, false);
		for(int i = 0; i < simulation.wave; ++i){
			spriteBatch.draw(level, 16*i, Settings.matricHeight - 16, 16, 16, 0, 0, 64, 64, false, false);
		}
		for(int i = 0; i < simulation.ship.lives; ++i){
			spriteBatch.draw(planeDemo, 16*i, Settings.matricHeight - 37, 16, 16, 0, 0, 64, 64, false, false);
		}
		spriteBatch.draw(award, 0, Settings.matricHeight - 58, 16, 16, 0, 0, 64, 64, false, false);
		font.draw(spriteBatch, Integer.toString(simulation.score), 16, Settings.matricHeight - 38);
		spriteBatch.end();
	}
	
	private void renderAward(GL10 gl, Simulation simulation){
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(award, Settings.matricWidth/2 - 32, Settings.matricHeight/2, 32, 32, 0, 0, 64, 64, false, false);
		spriteBatch.draw(planeDemo, Settings.matricWidth/2 - 32, Settings.matricHeight/2 - 40, 32, 32, 0, 0, 64, 64, false, false);
		font.draw(spriteBatch, " + " + simulation.awardScore, Settings.matricWidth/2, Settings.matricHeight/2 + 32);
		font.draw(spriteBatch, " + " + simulation.awardShip, Settings.matricWidth/2, Settings.matricHeight/2 - 8);
		spriteBatch.end();
		if(simulation.awardWait > 2){
			renderPlay();
		}
	}
	private void renderPlay(){
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);
		String strStart = "Touch to continue.";
		TextBounds bounds = font.getBounds(strStart);
		font.draw(spriteBatch, strStart, Settings.matricWidth/2 - bounds.width/2, Settings.matricHeight - 30);
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

	private void renderShip (GL10 gl, Ship ship, Application app) {
		if (ship.isExploding) return;

		shipTexture.bind();
		gl.glPushMatrix();
		gl.glTranslatef(ship.position.x, ship.position.y, ship.position.z);
		gl.glRotatef(45 * (-app.getInput().getAccelerometerY() / 5), 0, 0, 1);
		gl.glRotatef(180, 0, 1, 0);
		shipMesh.render(GL10.GL_TRIANGLES);
		gl.glPopMatrix();
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
	private void renderShield (GL10 gl, Ship ship) {
		gl.glEnable(GL10.GL_BLEND);
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBlendFunc(GL10.GL_ONE,GL10.GL_ONE);
		//shieldTexture.bind();
		gl.glColor4f(1, 1, 0, 1);
		Missile missile = new Missile(ship.position,true);
		//gl.glDepthMask(false);
		gl.glPushMatrix();
		gl.glTranslatef(missile.position.x, missile.position.y, missile.position.z);
		shieldMesh.render(GL10.GL_TRIANGLES);
		gl.glPopMatrix();
		//gl.glDepthMask(true);
		gl.glColor4f(1, 1, 1, 1);
		gl.glDisable(GL10.GL_BLEND);
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
		gl.glBlendFunc(GL10.GL_ONE_MINUS_DST_ALPHA, GL10.GL_DST_ALPHA);
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
