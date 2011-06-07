package com.badlogic.cubocy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public class LevelControlScreen extends CubocScreen {
	TextureRegion background;
	TextureRegion levelList;
	TextureRegion levelRepeat;
	TextureRegion nextLevel;
	Stage stage;
	public LevelControlScreen(Game game) {
		super(game);
		
	}
	
	@Override
	public void show() {
		stage = new Stage(240,160,true);
		Image imgBackground = new Image("Background Image",background);
		Button btnList = new Button("List Button", levelList);
		Button btnRepeat = new Button("Repeat Button", levelRepeat);
		Button btnNext = new Button("Next Button", nextLevel);
		stage.addActor(imgBackground);
		stage.addActor(btnList);
		stage.addActor(btnRepeat);
		stage.addActor(btnNext);
	}
	
	public void render(float deltaTime) {
		stage.draw();
		stage.act(deltaTime);
	}

	@Override
	public void hide() {
		stage.dispose();		
	}
}

