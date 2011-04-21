package com.badlogic.gdxinvaders.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Settings {
	public static float matricWidth = 480;
	public static float matricHeight = 320;
	private static float soundVolume = 0.7f;
	public static Music music;
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
	private static float musicVolume = 0.7f;
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

	public static int adCount = Integer.MAX_VALUE;
}
