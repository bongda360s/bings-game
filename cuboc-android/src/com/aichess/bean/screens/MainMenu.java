package com.aichess.bean.screens;

import com.aichess.bean.Assests;
import com.aichess.bean.Cubocy;
import com.aichess.bean.Settings;
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
		Assests.load();
	}

	@Override
	public void show() {
		stage = new Stage(480,320,true);		
		Image imgBackground = new Image("Bean Background",new TextureRegion(new Texture(Gdx.files.internal("data/beanbackground.png")),0,0,480,320));
		stage.addActor(imgBackground);
		
		String titlePath = "data/beantitle.png";
		if(System.getProperty("language").contains("zh"))
			titlePath = "data/beantitle.png";
		else
			titlePath = "data/beantitle-en.png";
		Image imgTitle = new Image("Bean Title",new TextureRegion(new Texture(Gdx.files.internal(titlePath)),0,0,250,65));
		imgTitle.x = 120;
		imgTitle.y = 190;
		imgTitle.scaleX = 0.8f;
		imgTitle.scaleY = 0.8f;
		stage.addActor(imgTitle);
		TextureRegion iconList[][] = new TextureRegion(new Texture(Gdx.files.internal("data/iconlist.png"))).split(64, 64);
		Button btnStart = new Button("Start Button",iconList[6][0],iconList[6][1]);
		btnStart.x = (480-64)/2;
		btnStart.y = (320-64)/2;
		stage.addActor(btnStart);
		btnStart.clickListener = new ClickListener(){
			@Override
			public void clicked(Button button) {
				Assests.clickSound.play(Settings.soundVolume);
				game.setScreen(new LevelChoiceScreen(game));				
		}};
		
		Button btnSetting = new Button("Start Button",iconList[3][0],iconList[3][1]);
		btnSetting.x = 10;
		btnSetting.y = 10;
		stage.addActor(btnSetting);
		btnSetting.clickListener = new ClickListener(){
			@Override
			public void clicked(Button button) {
				Assests.clickSound.play(Settings.soundVolume);
				if(((Cubocy)game).notifier!=null)
					((Cubocy)game).notifier.DialogNotify((Settings.settingID));	
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
		stage.clear();
		stage.dispose();
	}
}
