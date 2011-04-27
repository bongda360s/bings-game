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
    public static float matricWidth = 480;
	public static float matricHeight = 320;
	public static Music music;
	public static int appNo = 1;
	public static String phoneName;
	public static String[] heroNames = {"Hecate","Gaea","Uranus","Cronus","Rhea",
		"Oceanus","Tethys","Hyperion"};
    private static List<Fighting> fightings;
    /**
	 * @return the fightings
	 */
	public static List<Fighting> getFightings() {
		if(fightings==null || fightings.size() == 0){
			fightings = new ArrayList<Fighting>();
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

	private static List<Fighting> netFightings = null;
    
    public final static String file = "starwar.dat";
	
	private static float soundVolume = 0f;
	private static float musicVolume = 0f;
	private static int adCount = 5;	
	
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
    	fightings.add(fighting);
	    Collections.sort(fightings,new FightingComparator());
    }
    
    public static int getHighScore(String phoneName){
    	int highScore = 0;
    	for(int i = 0, length = fightings.size(); i < length; ++i){
    		if(fightings.get(i).getPhoneName().equals(phoneName)){
    			highScore = fightings.get(i).getScore();
    		}
    	}
    	return highScore;
    }
}


