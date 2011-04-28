package com.badlogic.gdxinvaders.simulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;

public class Settings {
    public static final float matricWidth = 480;
	public static final float matricHeight = 320;
	public static final int appNo = 1;
	public static final String[] heroNames = {"Hecate","Gaea","Uranus","Cronus","Rhea",
		"Oceanus","Tethys","Hyperion"};
	public final static String file = "starwar.dat";
	
	private static int adCount = 5;	
	private static Music music;
	private static String phoneName;
    private static List<Fighting> fightings;
	private static List<Fighting> netFightings;
	private static float soundVolume = 0f;
	private static float musicVolume = 0f;	
	private static int status = 1; //0:stop 1:playing 2:award
    /**
	 * @return the fightings
	 */
	public static synchronized List<Fighting> getFightings() {
		if(fightings==null || fightings.isEmpty()){
			fightings = new ArrayList<Fighting>(5);
			fightings.add(new Fighting("Zeus",1000,""));
	    	fightings.add(new Fighting("Hera",800,""));
	    	fightings.add(new Fighting("Poseidon",600,""));
	    	fightings.add(new Fighting("Hades",300,""));
	    	fightings.add(new Fighting("Demeter",100,""));
		}
		return fightings;
	}
	/**
	 * @param fightings the fightings to set
	 */
	public static void setFightings(List<Fighting> fightings) {
		Settings.fightings = fightings;
	}

	/**
	 * @return the netFightings
	 */
	public static List<Fighting> getNetFightings() {
		return netFightings;
	}
	/**
	 * @param netFightings the netFightings to set
	 */
	public static void setNetFightings(List<Fighting> netFightings) {
		Settings.netFightings = netFightings;
	}	
	
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

    public static void load() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(Gdx.files.external(file).read()));
                       
            soundVolume = Float.parseFloat(in.readLine());
            musicVolume = Float.parseFloat(in.readLine());
            adCount = Integer.parseInt(in.readLine());
            String strFight = in.readLine();
            Gson gson = new Gson();
            fightings = (List<Fighting>)gson.fromJson(strFight, fightings.getClass());
        } 
        catch (Exception e) {
            //nothing to do ,we have default...      
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public static void save() {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(Gdx.files.external(file).write(false)));
            
            out.write(Float.toString(soundVolume)+"\r\n");
            out.write(Float.toString(musicVolume)+"\r\n");
            out.write(Integer.toString(adCount)+"\r\n");
            Gson gson = new Gson();
            out.write(gson.toJson(fightings));

        } catch (Throwable e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }
    
    public static void addFighting(Fighting fighting){
    	getFightings().add(fighting);
	    Collections.sort(getFightings(),new FightingComparator());
    }
    
    public static int getHighScore(String phoneName){
    	int highScore = 0;
    	for(int i = 0, length = fightings.size(); i < length; ++i){
    		if(fightings.get(i).getPhoneName().equals(phoneName)){
    			highScore = fightings.get(i).getScore();
    			break;
    		}
    	}
    	return highScore;
    }
	/**
	 * @return the music
	 */
	public static Music getMusic() {
		return music;
	}
	/**
	 * @param music the music to set
	 */
	public static void setMusic(Music music) {
		Settings.music = music;
	}
	/**
	 * @return the phoneName
	 */
	public static String getPhoneName() {
		return phoneName;
	}
	/**
	 * @param phoneName the phoneName to set
	 */
	public static void setPhoneName(String phoneName) {
		Settings.phoneName = phoneName;
	}
	/**
	 * @return the status
	 */
	public static int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public static void setStatus(int status) {
		Settings.status = status;
	}
}


