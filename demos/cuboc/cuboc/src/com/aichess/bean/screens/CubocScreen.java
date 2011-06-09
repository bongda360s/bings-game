package com.aichess.bean.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class CubocScreen implements Screen {
	Game game;
	public CubocScreen(Game game) {
		this.game = game;
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}	
	
	public boolean onBackPressed(){
		return false;
	}
}
