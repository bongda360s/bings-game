package com.badlogic.gdxinvaders.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Settings {
	public static float matricWidth = 480;
	public static float matricHeight = 320;
	
	private static float soundVolume = 0.7f;
	private static float musicVolume = 0.7f;
	private static int adCount = 5;
	public static Music music;
	
	public static int getAdCount() {
		return adCount;
	}
	public static void setAdCount(int count) {
		adCount = count;
	}
	
	/**
	 * @return the soundVolume
	 */
	public static float getSoundVolume() {
		return soundVolume;
	}
	/**
	 * @param soundVolume the soundVolume to set
	 */
	public static void setSoundVolume(float volume) {
		soundVolume = volume;
	}
	
	/**
	 * @return the musicVolume
	 */
	public static float getMusicVolume() {
		return musicVolume;
	}
	/**
	 * @param musicVolume the musicVolume to set
	 */
	public static void setMusicVolume(float volume) {
		musicVolume = volume;
		if(music!=null)
			music.setVolume(volume);
	}

	public static void loadSettings(){
		
	}
	public static void saveSettings(){
		
	}
}
