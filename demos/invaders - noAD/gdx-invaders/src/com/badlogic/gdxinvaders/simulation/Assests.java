package com.badlogic.gdxinvaders.simulation;

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoader;

public class Assests {
	/** the background texture **/
	public static Texture background;
	/** the earth texture **/
	public static Texture earth;
	/** the logo texture **/
	public static Texture logo;	
	/** the font **/
	public static BitmapFont font;
	/** the ship mesh **/
	public static Mesh shipMesh;
	/** the ship texture **/
	public static Texture shipTexture;
	/** the invader mesh **/
	public static Mesh invaderMesh;
	/** the invader texture **/
	public static Texture invaderTexture;
	/** the block mesh **/
	public static Mesh blockMesh;
	/** the shot mesh **/
	public static Mesh shotMesh;
	/** the background texture **/
	public static Mesh missileMesh;
	//public static Texture backgroundTexture;
	/** the explosion mesh **/
	public static Mesh explosionMesh;
	/** the explosion texture **/
	public static Texture explosionTexture;
	/** the background music **/
	public static Music music;
	/** explosion sound **/
	public static Sound explosion;
	/** shot sound **/
	public static Sound shot;
	public static Sound missile;
	public static Music[] backgroundMusics = new Music[2];
	public static void load(){
		background = new Texture(Gdx.files.internal("data/background.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);				
		earth = new Texture(Gdx.files.internal("data/earth.png"));
		earth.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		logo = new Texture(Gdx.files.internal("data/title.png"));
		logo.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		font = new BitmapFont(Gdx.files.internal("data/font16.fnt"), Gdx.files.internal("data/font16.png"), false);
		try{
		InputStream in = Gdx.files.internal("data/ship.obj").read();
		shipMesh = ModelLoader.loadObj(in);
		in.close();

		in = Gdx.files.internal("data/invader.obj").read();
		invaderMesh = ModelLoader.loadObj(in);
		in.close();

		in = Gdx.files.internal("data/block.obj").read();
		blockMesh = ModelLoader.loadObj(in);
		in.close();

		in = Gdx.files.internal("data/shot.obj").read();
		shotMesh = ModelLoader.loadObj(in);
		in.close();
		
		in = Gdx.files.internal("data/shield.obj").read();
		missileMesh = ModelLoader.loadObj(in);
		in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		shipTexture = new Texture(Gdx.files.internal("data/ship.png"), true);
		shipTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);				
		invaderTexture = new Texture(Gdx.files.internal("data/invader.png"), true);
		invaderTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
//		backgroundTexture = new Texture(Gdx.files.internal("data/planet.jpg"), true);
//		backgroundTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		explosionTexture = new Texture(Gdx.files.internal("data/explode.png"), true);
		explosionTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		explosionMesh = new Mesh(true, 4 * 16, 0, new VertexAttribute(Usage.Position, 3, "a_position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord"));
		explosionMesh = new Mesh(true, 4 * 16, 0, new VertexAttribute(Usage.Position, 3, "a_position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord"));

			float[] vertices = new float[4 * 16 * (3 + 2)];
			int idx = 0;
			for (int row = 0; row < 4; row++) {
				for (int column = 0; column < 4; column++) {
					vertices[idx++] = 1;
					vertices[idx++] = 1;
					vertices[idx++] = 0;
					vertices[idx++] = 0.25f + column * 0.25f;
					vertices[idx++] = 0 + row * 0.25f;

					vertices[idx++] = -1;
					vertices[idx++] = 1;
					vertices[idx++] = 0;
					vertices[idx++] = 0 + column * 0.25f;
					vertices[idx++] = 0 + row * 0.25f;

					vertices[idx++] = -1;
					vertices[idx++] = -1;
					vertices[idx++] = 0;
					vertices[idx++] = 0f + column * 0.25f;
					vertices[idx++] = 0.25f + row * 0.25f;

					vertices[idx++] = 1;
					vertices[idx++] = -1;
					vertices[idx++] = 0;
					vertices[idx++] = 0.25f + column * 0.25f;
					vertices[idx++] = 0.25f + row * 0.25f;
				}
			}

			explosionMesh.setVertices(vertices);
			font = new BitmapFont(Gdx.files.internal("data/font10.fnt"), Gdx.files.internal("data/font10.png"), false);
			music = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/menu.ogg", FileType.Internal));
			explosion = Gdx.audio.newSound(Gdx.files.getFileHandle("data/explosion.ogg", FileType.Internal));
			shot = Gdx.audio.newSound(Gdx.files.getFileHandle("data/shot.ogg", FileType.Internal));
			missile = Gdx.audio.newSound(Gdx.files.getFileHandle("data/missile.ogg", FileType.Internal));
			backgroundMusics[0] = Gdx.audio.newMusic(Gdx.files.internal("data/background1.ogg"));
			backgroundMusics[1] = Gdx.audio.newMusic(Gdx.files.internal("data/background2.ogg"));
	}
	public static void dispose(){
		background.dispose();
		earth.dispose();
		logo.dispose();	
		font.dispose();
		shipMesh.dispose();
		shipTexture.dispose();
		invaderMesh.dispose();
		invaderTexture.dispose();
		blockMesh.dispose();
		shotMesh.dispose();
		missileMesh.dispose();
		//backgroundTexture.dispose();
		explosionMesh.dispose();
		explosionTexture.dispose();
		explosion.dispose();
		shot.dispose();
		missile.dispose();
		backgroundMusics[0].dispose();
		backgroundMusics[1].dispose();
	}
}
