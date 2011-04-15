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
		
		music = Gdx.audio.newMusic(Gdx.files.internal("data/pinball.ogg"));
		music.setLooping(true);
        music.setVolume(0.5f);         
        if(Settings.soundEnabled)
            music.play();
        
		gameReadySound = Gdx.audio.newSound(Gdx.files.internal("data/sound1.ogg"));
		ballReadySound = Gdx.audio.newSound(Gdx.files.internal("data/sound4.ogg"));
		impluseBallSound = Gdx.audio.newSound(Gdx.files.internal("data/sound45.ogg"));
		scoreSound = Gdx.audio.newSound(Gdx.files.internal("data/sound136.ogg"));
		rewardsSound = Gdx.audio.newSound(Gdx.files.internal("data/sound560.ogg"));
		gameoverSound = Gdx.audio.newSound(Gdx.files.internal("data/sound827.ogg"));
		ringSound = Gdx.audio.newSound(Gdx.files.internal("data/sound240.ogg"));
		flatterBallSound = Gdx.audio.newSound(Gdx.files.internal("data/sound34.ogg"));
		flatterSound = Gdx.audio.newSound(Gdx.files.internal("data/sound22.ogg"));
		rolloverSound = Gdx.audio.newSound(Gdx.files.internal("data/sound39.ogg"));
	}
	
	public static void playSound(Sound sound){
		if(Settings.soundEnabled)
			sound.play(1);
	}
}
