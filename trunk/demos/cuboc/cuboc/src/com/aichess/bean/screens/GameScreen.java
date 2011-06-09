package com.aichess.bean.screens;

import com.aichess.bean.Assests;
import com.aichess.bean.Cubocy;
import com.aichess.bean.Map;
import com.aichess.bean.MapRenderer;
import com.aichess.bean.OnscreenControlRenderer;
import com.aichess.bean.Settings;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener;

public class GameScreen extends CubocScreen {
	Map map;
	MapRenderer renderer;
	OnscreenControlRenderer controlRenderer;
	final int maxLevel = 16;
	Stage stage;
	boolean next = false;
	boolean pause = false;
	
	public GameScreen(Game game) {
		super(game);
	}
	
	@Override public void show () {
		map = new Map(((Cubocy)game).level);
		renderer = new MapRenderer(map);
		controlRenderer = new OnscreenControlRenderer(map);
		
		stage = new Stage(240,160,false);
		TextureRegion background = new TextureRegion(new Texture(Gdx.files.internal("data/dialogframe.png")),0,0,240,160);
		TextureRegion[][] regionList = new TextureRegion(new Texture(Gdx.files.internal("data/iconlist.png"))).split(64, 64);

		Image imgBackground = new Image("Background Image",background);
		Button btnList = new Button("List Button", regionList[4][0],regionList[4][1]);
		btnList.x = 60;
		btnList.y = 100;
		Button btnRepeat = new Button("Repeat Button", regionList[7][0],regionList[7][1]);
		btnRepeat.x = 120;
		btnRepeat.y = 100;
		Button btnPlay = new Button("Play Button", regionList[6][0],regionList[6][1]);
		btnPlay.x = 180;
		btnPlay.y = 100;
		
		btnList.clickListener = new ClickListener(){
			@Override
			public void clicked(Button button) {
				Assests.clickSound.play(Settings.soundVolume);
				game.setScreen(new LevelChoiceScreen(game));
		}};
		btnRepeat.clickListener = new ClickListener(){
			@Override
			public void clicked(Button button) {
				Assests.clickSound.play(Settings.soundVolume);
				game.setScreen(new GameScreen(game));
		}};
		btnPlay.clickListener = new ClickListener(){
			@Override
			public void clicked(Button button) {
				Assests.clickSound.play(Settings.soundVolume);
				if(pause)
					pause = false;
				else{
					((Cubocy)game).level++;
					game.setScreen(new GameScreen(game));
				}
		}};
		stage.addActor(imgBackground);
		stage.addActor(btnList);
		stage.addActor(btnRepeat);
		stage.addActor(btnPlay);		
	}
	
	@Override public void render (float delta) {
		if(next || pause){
			stage.draw();
			stage.act(delta);
		}
		else{
			delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
			map.update(delta);		
			Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);				
			renderer.render(delta);
			controlRenderer.render();
			
			if(map.bob.bounds.overlaps(map.endDoor.bounds)) {
				if(((Cubocy)game).level < maxLevel){				
					next = true;
					Gdx.input.setInputProcessor(stage);
				}
				else
					game.setScreen(new GameOverScreen(game));
			}
			
//			if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
//				pause = true;
//				Gdx.input.setInputProcessor(stage);
//			}
		}
	}
	@Override public boolean onBackPressed(){
		if(!pause)
			Gdx.input.setInputProcessor(stage);
		pause = !pause;
		return true;
	}
	@Override public void hide () {
		renderer.dispose();
		controlRenderer.dispose();
		stage.dispose();	
	}
}
