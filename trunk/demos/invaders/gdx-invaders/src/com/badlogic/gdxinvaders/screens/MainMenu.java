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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureDict;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdxinvaders.simulation.Settings;

/**
 * The main menu screen showing a background, the logo of the game and a label telling the user to touch the screen to start the
 * game. Waits for the touch and returns isDone() == true when it's done so that the ochestrating GdxInvaders class can switch to
 * the next screen.
 * @author mzechner
 * 
 */
public class MainMenu implements Screen {
	/** is done flag **/
	private boolean isDone = false;
	/** view & transform matrix **/
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();
	private Texture earth;
	/** the background music **/
	private Music music;
	/** the font **/
	private BitmapFont font;
	/** the background texture **/
	private Texture background;
	private Texture logo;
	private final SpriteBatch spriteBatch;
	public MainMenu (Application app) {
		this.spriteBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.GREEN);
		music = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/menu.ogg", FileType.Internal));
		music.setVolume(Settings.getMusicVolume());
		music.play();
		earth = TextureDict.loadTexture("data/earth.png").get();
		earth.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = TextureDict.loadTexture("data/background.png").get();
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		logo = TextureDict.loadTexture("data/title.png").get();
		logo.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override public void render (Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		viewMatrix.setToOrtho2D(0, 0, Settings.matricWidth, Settings.matricHeight);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);		
		spriteBatch.draw(background, 0, 0, Settings.matricWidth, Settings.matricHeight, 0, 0, 1024, 729, false, false);
		spriteBatch.enableBlending();
		spriteBatch.draw(earth, 60, 40, 360, 240, 0, 0, 512, 512, false, false);
		spriteBatch.draw(logo, 0, 320-128, 480, 128, 0, 0, 512, 256, false, false);
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.end();
		renderPlay();
	}
	private void renderPlay(){
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setColor(Color.WHITE);
		String strStart = "Touch to continue.";
		TextBounds bounds = font.getBounds(strStart);
		font.draw(spriteBatch, strStart, Settings.matricWidth/2 - bounds.width/2, Settings.matricHeight - 30);
		spriteBatch.end();
	}
	@Override public void update (Application app) {
		isDone = app.getInput().isTouched();
	}

	@Override public boolean isDone () {
		return isDone;
	}

	@Override public void dispose () {
		music.stop();
		font.dispose();
	}
}
