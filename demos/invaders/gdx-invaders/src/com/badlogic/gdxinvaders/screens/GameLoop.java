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
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoader;
import com.badlogic.gdxinvaders.Renderer;
import com.badlogic.gdxinvaders.simulation.Simulation;
import com.badlogic.gdxinvaders.simulation.SimulationListener;

public class GameLoop implements Screen, SimulationListener {
	/** the simulation **/
	private final Simulation simulation;
	/** the renderer **/
	private final Renderer renderer;
	
	private  Music[] backgroundMusics = new Music[2];
	/** explosion sound **/
	private  Sound explosion;
	/** shot sound **/
	private  Sound shot;
	
	
	public GameLoop (Application app) {
		backgroundMusics[0] = Gdx.audio.newMusic(Gdx.files.internal("data/background1.ogg"));
		backgroundMusics[1] = Gdx.audio.newMusic(Gdx.files.internal("data/background2.ogg"));
		explosion = Gdx.audio.newSound(Gdx.files.getFileHandle("data/explosion.ogg", FileType.Internal));
		shot = Gdx.audio.newSound(Gdx.files.getFileHandle("data/shot.ogg", FileType.Internal));

		simulation = new Simulation();
		simulation.listener = this;
		renderer = new Renderer(app);
	}

	@Override public void dispose () {
		renderer.dispose();
		shot.dispose();
		explosion.dispose();
		backgroundMusics[0].dispose();
		backgroundMusics[1].dispose();
	}

	@Override public boolean isDone () {
		return simulation.ship.lives <= 0;
	}

	@Override public void render (Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		renderer.render(app, simulation);
	}

	@Override public void update (Application app) {
		simulation.update(app.getGraphics().getDeltaTime());

		Input input = app.getInput();
		if (input.getAccelerometerY() < 0)
			simulation.moveShipLeft(app.getGraphics().getDeltaTime(), Math.abs(input.getAccelerometerY()) / 10);
		else
			simulation.moveShipRight(app.getGraphics().getDeltaTime(), Math.abs(input.getAccelerometerY()) / 10);

		if (input.isKeyPressed(Keys.KEYCODE_DPAD_LEFT)) simulation.moveShipLeft(app.getGraphics().getDeltaTime(), 0.5f);
		if (input.isKeyPressed(Keys.KEYCODE_DPAD_RIGHT)) simulation.moveShipRight(app.getGraphics().getDeltaTime(), 0.5f);

		if (input.isTouched() || input.isKeyPressed(Keys.KEYCODE_SPACE)) simulation.shot();
	}

	@Override public void explosion () {
		explosion.play();
	}

	@Override public void shot () {
		shot.play();
	}
}
