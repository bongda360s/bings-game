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
import com.badlogic.gdx.graphics.TextureDict;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdxinvaders.screens.GameLoop;
import com.badlogic.gdxinvaders.screens.GameOver;
import com.badlogic.gdxinvaders.screens.LoadingScreen;
import com.badlogic.gdxinvaders.screens.Screen;
import com.badlogic.gdxinvaders.screens.ShowLogo;
import com.badlogic.gdxinvaders.simulation.Fighting;
import com.badlogic.gdxinvaders.simulation.Settings;
import com.badlogic.gdxinvaders.simulation.SimulationListener;

public class GdxInvaders implements ApplicationListener  {	
	/** flag indicating whether we were initialized already **/
	private boolean isInitialized = false;
	/** the current screen **/
	private Screen screen;
	
//	public interface OnSubmitScoreListener{
//		void onSubmitListener(int score);
//	}
//	
//	private OnSubmitScoreListener submitScoreListener;
//	
//	public void setOnSubmitScoreListener(OnSubmitScoreListener onSubmitScoreListener){
//		submitScoreListener = onSubmitScoreListener;
//	}
	
	public GdxInvaders(){
	}
	
	@Override public void dispose () {
		Settings.save();
		TextureDict.unloadAll();
		screen.dispose();
	}

	@Override public void render () {
		Application app = Gdx.app;
		// update the screen
		screen.update(app);

		// render the screen
		screen.render(app);
		int score = 0;
		// when the screen is done we change to the
		// next screen
		if (screen.isDone()) {
			if(screen instanceof GameLoop){
				score = ((GameLoop)screen).getScore();
				Settings.addFighting(new Fighting(Settings.heroNames[(int)(Math.random()*(Settings.heroNames.length-1))], score, Settings.getPhoneName()));
			}
			// dispose the current screen
			screen.dispose();
			if (screen instanceof ShowLogo){
				screen = new LoadingScreen(app);
				Settings.setStatus(-1);
			}
			else
			// the game loop
			if (screen instanceof LoadingScreen){
				screen  = new GameLoop(app);
				Settings.setStatus(1);
			}
			else
			// game over screen
			if (screen instanceof GameLoop){
				screen = new GameOver(app);
				Settings.setStatus(-1);
			}
			else
			// main menu screen
			if (screen instanceof GameOver) {
				screen = new GameLoop(app);
				Settings.setStatus(1);
			}
		}
	}

	@Override public void resize (int width, int height) {

	}

	@Override public void create () {
		if (!isInitialized) {
			Settings.load();
			screen = new ShowLogo(Gdx.app);
			//screen = new LoadingScreen(Gdx.app);
			//loadAssests();
			//screen = new MainMenu(Gdx.app);
			isInitialized = true;			
		}
	}
	
	@Override
	public void pause() {
		Settings.save();	
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}
	
	
}
