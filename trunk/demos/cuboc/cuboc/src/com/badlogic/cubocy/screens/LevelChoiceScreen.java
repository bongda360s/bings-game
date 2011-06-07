package com.badlogic.cubocy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LevelChoiceScreen extends CubocScreen{
	Stage stage;
	public LevelChoiceScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		stage = new Stage(480,320,true);
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void render(float delta) {
		stage.draw();
		stage.act(delta);
	}

	@Override
	public void hide() {
		stage.dispose();
	}

}
