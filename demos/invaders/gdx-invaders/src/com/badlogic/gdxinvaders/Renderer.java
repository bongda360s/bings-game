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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdxinvaders.simulation.Assests;
import com.badlogic.gdxinvaders.simulation.Block;
import com.badlogic.gdxinvaders.simulation.Explosion;
import com.badlogic.gdxinvaders.simulation.Invader;
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
	private SpriteBatch spriteBatch;
	
	/** the rotation angle of all invaders around y **/
	private float invaderAngle = 0;
	/** status string **/
	private String status = "";
	/** keeping track of the last score so we don't constantly construct a new string **/
	private int lastScore = 0;
	private int lastLives = 0;
	private int lastWave = 0;
	
	/** view and transform matrix for text rendering **/
	private Matrix4 viewMatrix = new Matrix4();
	private Matrix4 transformMatrix = new Matrix4();

	/** perspective camera **/
	private PerspectiveCamera camera;

	public Renderer (Application app) {
		spriteBatch = new SpriteBatch();			
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());		
	}

	public void render (Application app, Simulation simulation) {
		GL10 gl = app.getGraphics().getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, app.getGraphics().getWidth(), app.getGraphics().getHeight());

		renderBackground(gl,simulation.ship.position);

		gl.glDisable(GL10.GL_DITHER);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_CULL_FACE);

		setProjectionAndCamera(app.getGraphics(), simulation.ship, app);
		setLighting(gl);

		gl.glEnable(GL10.GL_TEXTURE_2D);

		renderShip(gl, simulation.ship, app);
		renderInvaders(gl, simulation.invaders);

		gl.glDisable(GL10.GL_TEXTURE_2D);
		renderBlocks(gl, simulation.blocks);

		gl.glDisable(GL10.GL_LIGHTING);
		renderShots(gl, simulation.shots);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		renderExplosions(gl, simulation.explosions);

		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_DEPTH_TEST);

		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		if (simulation.ship.lives != lastLives || simulation.score != lastScore || simulation.wave != lastWave) {
			status = "lives: " + simulation.ship.lives + " wave: " + simulation.wave + " score: " + simulation.score;
			lastLives = simulation.ship.lives;
			lastScore = simulation.score;
			lastWave = simulation.wave;
		}
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Assests.font.draw(spriteBatch, status, 0, 320);		
		spriteBatch.end();

		invaderAngle += app.getGraphics().getDeltaTime() * 90;
		if (invaderAngle > 360) invaderAngle -= 360;
	}

	private void renderBackground (GL10 gl,Vector3 shipPosition) {
		viewMatrix.setToOrtho2D(0, 0, 480, 320);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(Assests.background, 0, 0, 480, 320, 0, 0, 1024, 729, false, false);
		spriteBatch.enableBlending();
		//System.out.println(shipPosition.x + " " + shipPosition.y + " " + shipPosition.z);
		spriteBatch.draw(Assests.earth, 60 - shipPosition.x, 40, 360, 240, 0, 0, 512, 512, false, false);
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

		Assests.shipTexture.bind();
		gl.glPushMatrix();
		//System.out.println(ship.position.x+" "+ ship.position.y+" "+ship.position.z);
		gl.glTranslatef(ship.position.x, ship.position.y, ship.position.z);
		gl.glRotatef(45 * (-app.getInput().getAccelerometerY() / 5), 0, 0, 1);
		gl.glRotatef(180, 0, 1, 0);
		Assests.shipMesh.render(GL10.GL_TRIANGLES);
		gl.glPopMatrix();
	}

	private void renderInvaders (GL10 gl, ArrayList<Invader> invaders) {
		Assests.invaderTexture.bind();
		for (int i = 0; i < invaders.size(); i++) {
			Invader invader = invaders.get(i);
			gl.glPushMatrix();
			gl.glTranslatef(invader.position.x, invader.position.y, invader.position.z);
			gl.glRotatef(invaderAngle, 0, 1, 0);
			Assests.invaderMesh.render(GL10.GL_TRIANGLES);
			gl.glPopMatrix();
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
			Assests.blockMesh.render(GL10.GL_TRIANGLES);
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
			Assests.shotMesh.render(GL10.GL_TRIANGLES);
			gl.glPopMatrix();
		}
		gl.glColor4f(1, 1, 1, 1);
	}

	private void renderExplosions (GL10 gl, ArrayList<Explosion> explosions) {
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Assests.explosionTexture.bind();
		for (int i = 0; i < explosions.size(); i++) {
			Explosion explosion = explosions.get(i);
			gl.glPushMatrix();
			gl.glTranslatef(explosion.position.x, explosion.position.y, explosion.position.z);
			Assests.explosionMesh.render(GL10.GL_TRIANGLE_FAN, (int)((explosion.aliveTime / Explosion.EXPLOSION_LIVE_TIME) * 15) * 4, 4);
			gl.glPopMatrix();
		}
		gl.glDisable(GL10.GL_BLEND);
	}

	public void dispose () {
		spriteBatch.dispose();
//		shipTexture.dispose();
//		invaderTexture.dispose();
//		backgroundTexture.dispose();
//		explosionTexture.dispose();
//		font.dispose();
//		explosionMesh.dispose();
//		shipMesh.dispose();
//		invaderMesh.dispose();
//		shotMesh.dispose();
//		blockMesh.dispose();
	}
}
