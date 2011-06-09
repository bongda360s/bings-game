package com.aichess.bean;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.aichess.bean.Cubocy;
import com.aichess.bean.screens.CubocScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.waps.AdView;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;

public class Cuboc extends AndroidApplication implements UpdatePointsNotifier {
	Game game;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //main view
        FrameLayout frameLayout = new FrameLayout(this);       
        FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);               
        mainLayoutParams.gravity = Gravity.CENTER;
        frameLayout.setLayoutParams(mainLayoutParams);         
        
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useWakelock = true;
        game = new Cubocy();
        View mainView = initializeForView(game, false);        
        mainView.setLayoutParams(createLayoutParams());
        frameLayout.addView(mainView);       
        
        //adcn
        /*
        AdViewLayout adViewLayout = new AdViewLayout(this, "SDK20110506530628h41f7cp3s595v19");
        FrameLayout.LayoutParams adviewLayoutParams = new FrameLayout.LayoutParams(420,LayoutParams.WRAP_CONTENT);
        adviewLayoutParams.gravity = Gravity.TOP;
        frameLayout.addView(adViewLayout, adviewLayoutParams);                
        adViewLayout.invalidate();
        */
        //wiyun
        /*
        com.wiyun.ad.AdView ad = new com.wiyun.ad.AdView(this);
		ad.setResId("9cba7263d77ef878");
		FrameLayout.LayoutParams adLayoutParams = new FrameLayout.LayoutParams(400, LayoutParams.WRAP_CONTENT);
        adLayoutParams.gravity = Gravity.TOP;
        ad.setLayoutParams(adLayoutParams);
		frameLayout.addView(ad);
		ad.requestAd();
		ad.setRefreshInterval(30);
		*/
        //waps ad
		AppConnect.getInstance(this);
		LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(420, LayoutParams.WRAP_CONTENT);
		containerParams.gravity = Gravity.TOP;
		LinearLayout container = new LinearLayout(Cuboc.this);		
		new AdView(this,container).DisplayAd(30);
		frameLayout.addView(container,containerParams);
		AppConnect.getInstance(Cuboc.this).getPoints(Cuboc.this);
        setContentView(frameLayout);        
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK){
        	if(((CubocScreen)game.getScreen()).onBackPressed())
        		return true;
    	}
        return super.onKeyDown(keyCode, event);
    }
	@Override
    protected void onDestroy() {
	  AppConnect.getInstance(this).finalize();
      super.onDestroy();
    }

	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		if(arg1 > 0){
			Settings.rememberStone += arg1;
			Settings.totalStone += arg1;
			AppConnect.getInstance(Cuboc.this).spendPoints(arg1, Cuboc.this);
		}		
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
	}
}