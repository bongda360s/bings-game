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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdxinvaders.simulation.Settings;

/**
 * The game over screen displays the final score and a game over text and waits for the user to touch the screen in which case it
 * will signal that it is done to the orchestrating GdxInvaders class.
 * 
 * @author mzechner
 * 
 */
public class GameOver implements Screen {
	/** is done flag **/
	private boolean isDone = false;
	/** view & transform matrix **/
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();
	/** the background texture **/
	private Texture background;	
	/** the logo texture **/
	private Texture logo;	
	/** the font **/
	private BitmapFont font;
	/** the background music **/
	private Music music;
	private final SpriteBatch spriteBatch;
	private TextureRegion continueRegion;
	public GameOver (Application app) {
		music = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/menu.ogg", FileType.Internal));
		music.setVolume(Settings.getMusicVolume());
		music.setLooping(true);
		music.play();
		Settings.setMusic(music);
		background = TextureDict.loadTexture("data/background.png").get();	 
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		logo = TextureDict.loadTexture("data/title.png").get();	
		logo.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		continueRegion = new TextureRegion(logo,98,385,204,28);
		font = new BitmapFont();
		font.setColor(1, 1, 0, 1);
		this.spriteBatch = new SpriteBatch();		
	}

	@Override public void dispose () {
		spriteBatch.dispose();
		font.dispose();
		music.stop();
	}

	@Override public boolean isDone () {
		return isDone;
	}

	@Override public void render (Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		viewMatrix.setToOrtho2D(0, 0, 480, 320);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.draw(background, 0, 0, 480, 320, 0, 0, 512, 512, false, false);
		spriteBatch.enableBlending();
		spriteBatch.draw(logo, 120, 120, 240, 80, 0, 0, 420, 140, false, false);
		spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);	
		spriteBatch.end();
		renderPlay();
	}
	private void renderPlay(){
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.draw(continueRegion, 180, 257,120,20);
		spriteBatch.end();
	}
	@Override public void update (Application app) {
		isDone = app.getInput().isTouched();
	}

}
