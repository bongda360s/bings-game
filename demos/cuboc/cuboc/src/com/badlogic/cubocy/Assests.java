package com.badlogic.cubocy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Assests {
	public static Sound deadSound;
	public static Music runMusic;
	//public static Sound laserSound;
	public static Sound launchSound,explosionSound;
	public static Music[] backgroundMusics = new Music[2];
	
	public static void load(){
		runMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/run.ogg", FileType.Internal));
		runMusic.setLooping(true);
		
		deadSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/dead.ogg", FileType.Internal));
		//laserSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/laser.ogg", FileType.Internal));
		backgroundMusics[0] = Gdx.audio.newMusic(Gdx.files.internal("data/background0.ogg"));
		backgroundMusics[0].setLooping(true);
		backgroundMusics[1] = Gdx.audio.newMusic(Gdx.files.internal("data/background1.ogg"));
		backgroundMusics[1].setLooping(true);
		launchSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/missile.wav", FileType.Internal));
		explosionSound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/explosion.ogg", FileType.Internal));
	}
	public static void dispose(){
		runMusic.dispose();
		deadSound.dispose();
		//laserSound.dispose();
		for(Music music : backgroundMusics) music.dispose();
		launchSound.dispose();
		explosionSound.dispose();
	}
}
