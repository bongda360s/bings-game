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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdxinvaders.screens.GameLoop;
import com.badlogic.gdxinvaders.screens.GameOver;
import com.badlogic.gdxinvaders.screens.LoadingScreen;
import com.badlogic.gdxinvaders.screens.MainMenu;
import com.badlogic.gdxinvaders.screens.Screen;

public class GdxInvaders implements ApplicationListener  {	
	/** flag indicating whether we were initialized already **/
	private boolean isInitialized = false;

	/** the current screen **/
	private Screen screen;

	@Override public void dispose () {	
	}

	@Override public void render () {
		Application app = Gdx.app;
		// update the screen
		screen.update(app);

		// render the screen
		screen.render(app);

		// when the screen is done we change to the
		// next screen
		if (screen.isDone()) {
			// dispose the current screen
			screen.dispose();
			
			// the game loop
			if (screen instanceof LoadingScreen)
				screen  = new MainMenu(app);
			else
			// if this screen is a main menu screen we switch to	
			if (screen instanceof MainMenu)
				screen = new GameLoop(app);
			else
			// if this screen is a game loop screen we switch to the
			// game over screen
			if (screen instanceof GameLoop)
				screen = new GameOver(app);
			else
			// if this screen is a game over screen we switch to the
			// main menu screen
			if (screen instanceof GameOver) screen = new MainMenu(app);
		}
	}

	@Override public void resize (int width, int height) {

	}

	@Override public void create () {
		if (!isInitialized) {	
			screen = new LoadingScreen(Gdx.app);
			//loadAssests();
			//screen = new MainMenu(Gdx.app);
			isInitialized = true;			
		}
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}
	
	
}
