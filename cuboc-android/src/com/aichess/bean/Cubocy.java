package com.aichess.bean;

import com.aichess.bean.screens.MainMenu;
import com.badlogic.gdx.Game;

public class Cubocy extends Game {	
	public ShowDialogNotify notifier;
	public int level;
	
//	public static void main(String[] argv) {
//		new LwjglApplication(new Cubocy(), "Cubocy", 480, 320, false);
//	}	
	
	@Override public void create () {	
		Settings.load();
		level = Settings.activeLevel;
		setScreen(new MainMenu(this));
	}
	
	@Override public void dispose () {
		if(level > Settings.activeLevel)
			Settings.activeLevel = level;
		Settings.save();
		Assests.dispose();
		super.dispose();
	}
	
	public void setDialogNotify(ShowDialogNotify dialogNotify){
		this.notifier = dialogNotify;
	}
}
