package com.badlogic.gdxinvaders.screens;

import java.util.prefs.BackingStoreException;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureDict;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdxinvaders.simulation.Settings;

public class LoadingScreen implements Screen {
	
	private boolean animationCompleted = false;
	/** view & transform matrix **/
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transformMatrix = new Matrix4();
	private float earthLeft = 64;
	private float earthTop = 30;
	private float earthWidth = 384;
	private float earthHeight = 256;
	private float localX = 0;
	private final float totalAnimateTime = 1;
	private Texture earth;
	private Texture background;
	private final SpriteBatch spriteBatch;
	private boolean isDone = false;
	/** the background music **/
	private Music music;
	
	public LoadingScreen(Application app){
		music = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/menu.ogg", FileType.Internal));
		music.setVolume(Settings.getMusicVolume());
		music.setLooping(true);
		music.play();
		Settings.music = music;
		this.spriteBatch = new SpriteBatch();
		earth = TextureDict.loadTexture("data/earth.png").get();
		earth.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = TextureDict.loadTexture("data/background.png").get();
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
	}
	
	@Override
	public void update(Application app) {
		isDone = app.getInput().isTouched();
	}
	private void updateEarth(Application app){
		float delta;
		if(!animationCompleted){
			delta = app.getGraphics().getDeltaTime();
			earthLeft -= 64 * delta / totalAnimateTime;
			earthTop -= 30 * delta / totalAnimateTime;
			earthWidth += (512-384) * delta / totalAnimateTime;
			earthHeight += (512-256) * delta / totalAnimateTime;
			if(earthLeft <=32)
				localX += 120 * delta / totalAnimateTime;
		}
		if(earthLeft <= 0 || earthTop <= 0 || earthWidth >= 512 || earthHeight >=512 || localX >=60){
			earthLeft = 0;
			earthTop = 0;
			earthWidth = 512;
			earthHeight = 512;
			localX = 60;
			animationCompleted = true;
		}
	}
	@Override
	public void render(Application app) {
		// TODO Auto-generated method stub
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		viewMatrix.setToOrtho2D(0, 0, Settings.matricWidth, Settings.matricHeight);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.setTransformMatrix(transformMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);		
		spriteBatch.draw(background, 0, 0, Settings.matricWidth, Settings.matricHeight, 0, 0, 1024, 729, false, false);
		spriteBatch.enableBlending();
		spriteBatch.draw(earth, localX - Gdx.input.getAccelerometerY(), 40 - Gdx.input.getAccelerometerX(), Settings.matricWidth - 2*localX, 240, (int)earthLeft, (int)earthTop, (int)earthWidth, (int)earthHeight, false, false);
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);		
		spriteBatch.end();
		updateEarth(app);
	}
	
	@Override
	public boolean isDone() {
		return isDone;
//		if(animationCompleted){			
//			return true;
//		}else
//			return false;
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		music.stop();
	}

}
