package com.badlogic.gdxinvaders.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureDict;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdxinvaders.simulation.Settings;


public class ShowLogo implements Screen {
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();
	private Texture background;
	private final SpriteBatch spriteBatch;
	private float elapseTime = 0;
	private BitmapFont font;
	public ShowLogo(Application app){		
		background = TextureDict.loadTexture("data/background.png").get();
		background.setFilter(TextureFilter.Linear,TextureFilter.Linear);
		font = new BitmapFont();
		this.spriteBatch = new SpriteBatch();
	}
	
	@Override
	public void update(Application app) {
		elapseTime += Gdx.graphics.getDeltaTime();
	}

	@Override
	public void render(Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		viewMatrix.setToOrtho2D(0, 0, Settings.matricWidth, Settings.matricHeight);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);		
		spriteBatch.draw(background, 0, 0, Settings.matricWidth, Settings.matricHeight, 0, 0, 1024, 729, false, false);
		font.setColor(1, 1, 0, 1);
		//font.setScale(2f);
		String str = "L O A D I N G . . .";
		TextBounds bound = font.getBounds(str);
		font.draw(spriteBatch, str, Settings.matricWidth/2- bound.width/2, bound.height + 20);
		//font.setScale(0.5f);
		spriteBatch.end();
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return elapseTime > 2;
	}

	@Override
	public void dispose() {	
		spriteBatch.dispose();
	}
}
