package com.aichess.starwar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Starwar extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(R.layout.main);
		new Thread() {
			public void run() {
				try {
						sleep(2000); 
				        Intent intent = new Intent();
				        intent.setClass(getApplicationContext(), GdxInvadersAndroid.class);   
				        startActivity(intent); 
				        System.runFinalizersOnExit(true);
						System.exit(0);
			        }
			        catch (Exception e) {
			        	e.printStackTrace();
			        }
				}       
        	}.start();	
	}
}
