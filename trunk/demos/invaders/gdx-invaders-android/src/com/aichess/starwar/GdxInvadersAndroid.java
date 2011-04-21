
package com.aichess.starwar;

import net.youmi.android.AdListener;
import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdxinvaders.GdxInvaders;
import com.badlogic.gdxinvaders.simulation.Invader;
import com.badlogic.gdxinvaders.simulation.Settings;

public class GdxInvadersAndroid extends AndroidApplication implements AdListener {
	private final int settingID = 1;
	private final int bulletinID = 2;
	
	static{ 
    	AdManager.init("f67d5f8c4e102945", "46ffddb0a2f1cf4d", 31, false,"1.0");   
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
	/** Called when the activity is first created. */
	@Override public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        FrameLayout frameLayout = new FrameLayout(this);       
        FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);               
        mainLayoutParams.gravity = Gravity.CENTER;
        frameLayout.setLayoutParams(mainLayoutParams);
        
        View view = initializeForView(new GdxInvaders(), false, new FillResolutionStrategy(), 20);        
        view.setLayoutParams(createLayoutParams());
        frameLayout.addView(view);

        //youmi ad
        adView = new AdView(this,Color.GRAY, Color.WHITE, 100);
		adView.setAdListener(this);
		FrameLayout.LayoutParams adViewParams = new FrameLayout.LayoutParams(360, 38);      
		adViewParams.gravity = Gravity.RIGHT;
		adView.setLayoutParams(adViewParams);
		adView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Settings.adCount--;
				if(Settings.adCount==0)
					adView.setVisibility(View.INVISIBLE);
			}
		});
        frameLayout.addView(adView);
        
        ImageView settingsView = new ImageView(GdxInvadersAndroid.this);
        settingsView.setImageResource(R.drawable.settings);
        settingsView.setAlpha(180);
        FrameLayout.LayoutParams settingsParams = new FrameLayout.LayoutParams(48, 48);
        settingsParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        settingsView.setLayoutParams(settingsParams);
        settingsView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(settingID);
			}
		});
        frameLayout.addView(settingsView);
        
        ImageView bulletinView = new ImageView(GdxInvadersAndroid.this);
        bulletinView.setImageResource(R.drawable.worldmap);
        bulletinView.setAlpha(180);
        FrameLayout.LayoutParams bulletinParams = new FrameLayout.LayoutParams(48, 48);
        bulletinParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        bulletinView.setLayoutParams(settingsParams);
        bulletinView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        frameLayout.addView(bulletinView);
        
        setContentView(frameLayout);

    }
    
    protected FrameLayout.LayoutParams createLayoutParams() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        layoutParams.gravity = Gravity.TOP;
        return layoutParams;
    }

	@Override
	protected Dialog onCreateDialog(int id) {
		LayoutInflater inflater = getLayoutInflater();
		View view;
		switch(id){
		case settingID:
			view = inflater.inflate(R.layout.settings, null);
			new AlertDialog.Builder(GdxInvadersAndroid.this)
			.setView(view)
			.show();
			return null;
		case bulletinID:
			return null;
		}
		return super.onCreateDialog(id);
	}
    
    
}