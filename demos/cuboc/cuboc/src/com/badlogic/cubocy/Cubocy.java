package com.badlogic.cubocy;

import com.badlogic.cubocy.screens.CubocScreen;
import com.badlogic.cubocy.screens.GameScreen;
import com.badlogic.cubocy.screens.MainMenu;
import com.badlogic.gdx.Game;

public class Cubocy extends Game {	
//	public static void main(String[] argv) {
//		new LwjglApplication(new Cubocy(), "Cubocy", 480, 320, false);
//	}

	@Override public void create () {		
		setScreen(new MainMenu(this));
	}
	
	@Override public void render () {
		super.render();
		CubocScreen cubocScreen = (CubocScreen)this.getScreen();
		if(cubocScreen.isDone()){ 
			if(cubocScreen instanceof MainMenu){
				Assests.load();
				cubocScreen.hide();
				setScreen(new GameScreen(this));
			}else if(cubocScreen instanceof GameScreen){
				cubocScreen.hide();
				setScreen(new MainMenu(this));
			}
		}
	}
	
	@Override public void dispose () {
		Assests.dispose();
		super.dispose();
	}
}
