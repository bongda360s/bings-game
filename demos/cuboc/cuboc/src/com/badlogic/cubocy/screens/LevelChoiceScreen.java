package com.badlogic.cubocy.screens;

import com.badlogic.cubocy.Cubocy;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public class LevelChoiceScreen extends CubocScreen{
	Stage stage;
	TextureRegion[] levels;
	Button[] btnLevels;
	Image imgBackground;
	public LevelChoiceScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		stage = new Stage(480,320,true);		
		imgBackground = new Image("Bean Background",new TextureRegion(new Texture(Gdx.files.internal("data/beanbackground.png"))).split(480, 320)[0][0]);
		stage.addActor(imgBackground);
		levels = new TextureRegion(new Texture(Gdx.files.internal("data/gameLevel.png"))).split(40, 36)[0];
		int level = ((Cubocy)game).level;
		btnLevels = new Button[16];
		for(int i = 0; i < btnLevels.length; i++){
			if(i > level)
				btnLevels[i] = new Button(Integer.toString(i),levels[18]);
			else{
				btnLevels[i] = new Button(Integer.toString(i),levels[i]);
				btnLevels[i].clickListener = new ClickListener(){
					@Override
					public void clicked(Button button) {
						((Cubocy)game).level = Integer.parseInt(button.name);
						game.setScreen(new GameScreen(game));
					}};
			}
			btnLevels[i].x = (i % 4) * 80 + 20;
			btnLevels[i].y = 320 - ((i/4) * 60 + 100);
			stage.addActor(btnLevels[i]);
		}
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
