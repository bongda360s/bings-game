package com.aichess.bean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
	public static float soundVolume = 0.7f;
	public static float musicVolume = 0.8f;
	public static int rememberStone = 10;
	public static int level = 0;
	public static String NAME = "com.aichess.bean";
	public static void load(){
		Preferences  pref = Gdx.app.getPreferences(NAME);
		soundVolume = pref.getFloat("soundVolume", 0.7f);
		musicVolume = pref.getFloat("musicVolume",0.8f);
		rememberStone = pref.getInteger("rememberStone", 10);
		level = pref.getInteger("level",0);
	}
	public static void save(){
		Preferences  pref = Gdx.app.getPreferences(NAME);
		pref.putFloat("soundVolume", soundVolume);
		pref.putFloat("musicVolume",musicVolume);
		pref.putInteger("rememberStone", rememberStone);
		pref.putInteger("level",level);
		pref.flush();
	}
}
