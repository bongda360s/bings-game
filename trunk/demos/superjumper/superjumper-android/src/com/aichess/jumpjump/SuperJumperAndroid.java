package com.aichess.jumpjump;

import net.youmi.android.AdListener;
import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AndroidFiles;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.badlogic.gdx.backends.android.AndroidInput;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogicgames.superjumper.SuperJumper;

public class SuperJumperAndroid extends AndroidApplication implements AdListener{
	
	static{ 
    	AdManager.init("3cc0b18985648cb0", "6db73c99cfc2add4", 31, false,"1.6");   
    }
	
	AdView adView;
	
	@Override
	public void onConnectFailed() {
		// TODO Auto-generated method stub
		System.out.println("on conncet failed");
	}

	@Override
	public void onReceiveAd() {
		// TODO Auto-generated method stub
		//adView.setVisibility(View.INVISIBLE);
		System.out.println("on ReceiveAd");
	}
	
	//private String MY_AD_UNIT_ID = "a14da2f83bc217d";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        FrameLayout frameLayout = new FrameLayout(this);       
        FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);               
        mainLayoutParams.gravity = Gravity.CENTER;
        frameLayout.setLayoutParams(mainLayoutParams);
        
        View view = initializeForView(new SuperJumper(), false, new FillResolutionStrategy(), 0);        
        view.setLayoutParams(createLayoutParams());
        frameLayout.addView(view);
        /*
        Button btnNew = new Button(this);
        btnNew.setText("new button");
        FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);      
        btnParams.gravity = Gravity.BOTTOM;
        btnNew.setLayoutParams(btnParams);
        frameLayout.addView(btnNew);       
        */
        //youmi ad
        adView = new AdView(this,Color.GRAY, Color.WHITE, 100);
		adView.setAdListener(this);
		FrameLayout.LayoutParams adViewParams = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);      
		adViewParams.gravity = Gravity.BOTTOM;
		adView.setLayoutParams(adViewParams);
        frameLayout.addView(adView);
        
        setContentView(frameLayout);
        
        
        /*ADView
        AdViewManager.setConfigExpireTimeout(-1);  
        AdViewTargeting.setTestMode(false);            
        AdViewLayout adViewLayout = new AdViewLayout(this, "SDK20112311470452fuch0w1ffdtssz7");
        getWindow().setContentView(adViewLayout, createLayoutParams());
        */
        
        /*
        // Create the adView
        AdView adView = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);
        
//        Button btnNew = new Button(this);
//        btnNew.setText("new button");
//        btnNew.setId(1);
        
        
        // Lookup your LinearLayout assuming it��s been given
        // the attribute android:id="@+id/mainLayout"
        //LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        
        this.getWindow().addContentView(adView, createLayoutParams());
        // Add the adView to it
        //layout.addView(adView);
        // Initiate a generic request to load it with an ad
        adView.loadAd(new AdRequest());
        */
    }
    
    protected FrameLayout.LayoutParams createLayoutParams() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        layoutParams.gravity = Gravity.BOTTOM;
        return layoutParams;
    }
    
}