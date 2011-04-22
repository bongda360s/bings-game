
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdxinvaders.GdxInvaders;
import com.badlogic.gdxinvaders.simulation.Invader;
import com.badlogic.gdxinvaders.simulation.Settings;

public class GdxInvadersAndroid extends AndroidApplication implements AdListener {
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		
//		new AlertDialog.Builder(GdxInvadersAndroid.this)
//		.setMessage("��ȷ��Ҫ�뿪��Ϸ��")
//		.setIcon(android.R.drawable.btn_dialog)
//		.setNegativeButton(arg0, arg1);
		super.onBackPressed();
	}

	private final int settingID = 1;
	private final int bulletinID = 2;
	private View view;
	static{ 
    	AdManager.init("f67d5f8c4e102945", "46ffddb0a2f1cf4d", 31, false,"1.0");   
    }
	
	AdView adView;
	
	@Override
	public void onConnectFailed() {
	}

	@Override
	public void onReceiveAd() {
	}
	
	/** Called when the activity is first created. */
	@Override public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.setContentView(R.layout.main);
        
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
        //settingsView.setAlpha(180);
        FrameLayout.LayoutParams settingsParams = new FrameLayout.LayoutParams(72, 72);
        settingsParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        settingsView.setLayoutParams(settingsParams);
        settingsParams.setMargins(10, 0, 0, 7);
        settingsView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(settingID);
			}
		});
        
        frameLayout.addView(settingsView);
        
        ImageView bulletinView = new ImageView(GdxInvadersAndroid.this);
        bulletinView.setImageResource(R.drawable.rss_icon);
        //bulletinView.setAlpha(180);
        FrameLayout.LayoutParams bulletinParams = new FrameLayout.LayoutParams(72, 72);
        bulletinParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        bulletinParams.setMargins(0, 0, 10, 7);
        bulletinView.setLayoutParams(bulletinParams);
        bulletinView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
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
		switch(id){
		case settingID:
			view = inflater.inflate(R.layout.settings, null);
	        SeekBar barAD = (SeekBar)view.findViewById(R.id.barAD);
	        barAD.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	
				}				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					TextView txtAD = (TextView)view.findViewById(R.id.txtAD);
					txtAD.setText(String.format("激活%d个广告后，广告条消失", seekBar.getProgress()+1));
				}
			});
	        
	        SeekBar barMusic = (SeekBar)view.findViewById(R.id.barMusic);
	        barMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					Settings.setMusicVolume(seekBar.getProgress()/100f);
				}				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	
				}				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
				}
			});
	        
	        SeekBar barSound = (SeekBar)view.findViewById(R.id.barSound);
	        barSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					Settings.setSoundVolume(seekBar.getProgress()/100f);
				}				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	
				}				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
				}
			});
	        
			return new AlertDialog.Builder(GdxInvadersAndroid.this)
			.setView(view)
			.show();			
		case bulletinID:
			return null;
		}
		return super.onCreateDialog(id);
	}
}