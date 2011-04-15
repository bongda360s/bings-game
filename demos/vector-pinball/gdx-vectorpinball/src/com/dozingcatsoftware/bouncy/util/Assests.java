package com.dozingcatsoftware.bouncy.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Assests {
	
	public static Music music;
	public static Sound gameReadySound;
	public static Sound ballReadySound;
	public static Sound impluseBallSound;
	public static Sound gameoverSound;
	public static Sound scoreSound;
	public static Sound rewardsSound;
	public static Sound ringSound;
	public static Sound flatterBallSound;
	public static Sound flatterSound;
	public static Sound rolloverSound;
	
	public static void load(){
		
		music = Gdx.audio.newMusic(Gdx.files.internal("data/pinball.mp3"));
		music.setLooping(true);
        music.setVolume(0.5f);         
        if(Settings.soundEnabled)
            music.play();
        
		gameReadySound = Gdx.audio.newSound(Gdx.files.internal("data/sound1.mp3"));
		ballReadySound = Gdx.audio.newSound(Gdx.files.internal("data/sound4.mp3"));
		impluseBallSound = Gdx.audio.newSound(Gdx.files.internal("data/sound45.mp3"));
		scoreSound = Gdx.audio.newSound(Gdx.files.internal("data/sound136.mp3"));
		rewardsSound = Gdx.audio.newSound(Gdx.files.internal("data/sound560.mp3"));
		gameoverSound = Gdx.audio.newSound(Gdx.files.internal("data/sound827.mp3"));
		ringSound = Gdx.audio.newSound(Gdx.files.internal("data/sound240.mp3"));
		flatterBallSound = Gdx.audio.newSound(Gdx.files.internal("data/sound34.mp3"));
		flatterSound = Gdx.audio.newSound(Gdx.files.internal("data/sound22.mp3"));
		rolloverSound = Gdx.audio.newSound(Gdx.files.internal("data/sound39.mp3"));
	}
	
	public static void playSound(Sound sound){
		if(Settings.soundEnabled)
			sound.play(1);
	}
}
