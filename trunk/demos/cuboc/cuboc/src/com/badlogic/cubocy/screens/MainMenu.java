package com.badlogic.cubocy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener;

public class MainMenu extends CubocScreen {
	Stage stage;
	public MainMenu(Game game) {
		super(game);
	}

	@Override
	public void show() {
		stage = new Stage(480,320,true);		
		Image imgBackground = new Image("Bean Background",new TextureRegion(new Texture(Gdx.files.internal("data/beanbackground.png")),0,0,480,320));
		stage.addActor(imgBackground);
		Button btnStart = new Button("Start Button");
		stage.addActor(btnStart);
		btnStart.clickListener = new ClickListener(){
			@Override
			public void clicked(Button button) {
				game.setScreen(new LevelChoiceScreen(game));				
			}};
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
