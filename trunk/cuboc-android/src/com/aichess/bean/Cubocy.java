package com.aichess.bean;

import com.aichess.bean.screens.MainMenu;
import com.badlogic.gdx.Game;

public class Cubocy extends Game {	
	public int level;
	
//	public static void main(String[] argv) {
//		new LwjglApplication(new Cubocy(), "Cubocy", 480, 320, false);
//	}
	
	@Override public void create () {	
		Settings.load();
		level = Settings.level;
		setScreen(new MainMenu(this));
	}
	
	@Override public void dispose () {
		Settings.level = level;
		Settings.save();
		Assests.dispose();
		super.dispose();
	}
	
}
