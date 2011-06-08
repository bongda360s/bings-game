package com.badlogic.cubocy.screens;

import com.badlogic.cubocy.Cubocy;
import com.badlogic.cubocy.Map;
import com.badlogic.cubocy.MapRenderer;
import com.badlogic.cubocy.OnscreenControlRenderer;
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
		
		stage = new Stage(240,160,true);
		TextureRegion background = new TextureRegion(new Texture(Gdx.files.internal("data/frame.png")),0,0,240,160);
		TextureRegion[][] regionList = new TextureRegion(new Texture(Gdx.files.internal("data/iconList.png"))).split(64, 64);

		Image imgBackground = new Image("Background Image",background);
		Button btnList = new Button("List Button", regionList[0][0],regionList[0][1]);
		Button btnRepeat = new Button("Repeat Button", regionList[1][0],regionList[1][1]);
		Button btnPlay = new Button("Next Button", regionList[2][0],regionList[2][1]);
		btnList.clickListener = new ClickListener(){
			@Override
			public void clicked(Button button) {
				game.setScreen(new LevelChoiceScreen(game));
		}};
		btnRepeat.clickListener = new ClickListener(){
			@Override
			public void clicked(Button button) {
				game.setScreen(new GameScreen(game));
		}};
		btnPlay.clickListener = new ClickListener(){
			@Override
			public void clicked(Button button) {
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
			
			if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
				pause = true;
				Gdx.input.setInputProcessor(stage);
			}
		}
	}
	
	@Override public void hide () {
		System.out.println("dispose game screen");
		renderer.dispose();
		controlRenderer.dispose();
		stage.dispose();	
	}
}
