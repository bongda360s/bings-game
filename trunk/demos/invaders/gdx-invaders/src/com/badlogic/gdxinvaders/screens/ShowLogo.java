package com.badlogic.gdxinvaders.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureDict;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdxinvaders.simulation.Settings;


public class ShowLogo implements Screen {
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();
	private Texture background;
	private final SpriteBatch spriteBatch;
	private float elapseTime = 0;
	public ShowLogo(Application app){		
		background = TextureDict.loadTexture("data/background.png").get();	
		this.spriteBatch = new SpriteBatch();
	}
	
	@Override
	public void update(Application app) {
		// TODO Auto-generated method stub
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
		spriteBatch.end();
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return elapseTime > 1;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub		
		spriteBatch.dispose();
	}
}
