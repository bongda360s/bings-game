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

package com.badlogic.gdxinvaders.screens;

import java.io.InputStream;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoader;
import com.badlogic.gdxinvaders.Renderer;
import com.badlogic.gdxinvaders.simulation.Settings;
import com.badlogic.gdxinvaders.simulation.Simulation;
import com.badlogic.gdxinvaders.simulation.SimulationListener;

public class GameLoop implements Screen, SimulationListener {	
	/** the simulation **/
	private final Simulation simulation;
	/** the renderer **/
	private final Renderer renderer;	
	/** explosion sound **/
	private  Sound explosion;
	/** shot sound **/
	private  Sound shot;
	private Sound shieldSound;
	private Sound missile;
	public GameLoop (Application app) {
		explosion = Gdx.audio.newSound(Gdx.files.internal("data/explosion.ogg"));
		shot = Gdx.audio.newSound(Gdx.files.internal("data/shot.ogg"));
		shieldSound = Gdx.audio.newSound(Gdx.files.internal("data/shield.ogg"));
		missile = Gdx.audio.newSound(Gdx.files.internal("data/missile.wav"));
		simulation = new Simulation();
		simulation.listener = this;
		renderer = new Renderer(app);
	}

	@Override public void dispose () {
		missile.dispose();
		shot.dispose();
		explosion.dispose();
		shieldSound.dispose();
		renderer.dispose();
		simulation.dispose();
	}

	@Override public boolean isDone () {
		return simulation.isGameOver();
	}

	@Override public void render (Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		renderer.render(app, simulation);
	}

	@Override public void update (Application app) {
		updateApp(app);			
	}

	private void updateApp(Application app) {	
		Input input = app.getInput();
		simulation.update(app.getGraphics().getDeltaTime());	
		if(Settings.getStatus()==1){				
			if (input.getAccelerometerY() < 0)
				simulation.moveShipLeft(app.getGraphics().getDeltaTime(), Math.abs(input.getAccelerometerY()) / 10);
			else
				simulation.moveShipRight(app.getGraphics().getDeltaTime(), Math.abs(input.getAccelerometerY()) / 10);
	
			if (input.isKeyPressed(Keys.DPAD_LEFT)) simulation.moveShipLeft(app.getGraphics().getDeltaTime(), 0.5f);
			if (input.isKeyPressed(Keys.DPAD_RIGHT)) simulation.moveShipRight(app.getGraphics().getDeltaTime(), 0.5f);
	
			if (input.isTouched() || input.isKeyPressed(Keys.SPACE)){
				simulation.shot();
				//simulation.launch();
			}
			if (input.getAccelerometerX() < 0) 
				simulation.launch();
			if (Math.abs(input.getAccelerometerY()) > 5) 
				simulation.shield();
		}
		else if(input.isTouched() || input.isKeyPressed(Keys.SPACE)){
			if(Settings.getStatus()==0 || (Settings.getStatus()==2 && simulation.awardWait > 2))
				Settings.setStatus(1);
		}
	}

	@Override public void explosion () {
		explosion.play(Settings.getSoundVolume());
	}

	@Override public void shot () {
		shot.play(Settings.getSoundVolume());
	}
	@Override public void launch(){
		missile.play(Settings.getSoundVolume());
	}
	@Override public int getScore(){
		return simulation.score;
	}

	@Override
	public void shield() {
		// TODO Auto-generated method stub
		shieldSound.play(Settings.getSoundVolume());
	}
}
