package com.aichess.bean;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aichess.bean.Cubocy;
import com.aichess.bean.screens.CubocScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.waps.AdView;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;

public class Cuboc extends AndroidApplication implements UpdatePointsNotifier, ShowDialogNotify {
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
        ((Cubocy)game).setDialogNotify(Cuboc.this);
        //waps ad
        AppConnect.getInstance(this);
		LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(420,LayoutParams.WRAP_CONTENT);
		containerParams.gravity = Gravity.LEFT;
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
	protected void onResume(){
		AppConnect.getInstance(Cuboc.this).getPoints(Cuboc.this);
		super.onResume();
	}
	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		Settings.totalStone = arg1;
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
	}

	@Override
	public void DialogNotify(final int id) {
		this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				if(id==Settings.settingID)
					showDialog(id);	
				else if(id == Settings.stoneID)
					AppConnect.getInstance(Cuboc.this).showOffers(Cuboc.this);
			}});		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {	
		LayoutInflater inflater = getLayoutInflater();
		switch(id){		
		case Settings.settingID:
			View view = inflater.inflate(R.layout.settings, null);
			TextView txtInstall = (TextView)view.findViewById(R.id.txtInstall);
			txtInstall.setText(String.format(getResources().getText(R.string.install_desc).toString(),
					Settings.totalStone-Settings.rememberStone,
					Settings.totalStone,
					Settings.UNLOCKSTONE-Settings.totalStone));
			Button btnInstall = (Button)view.findViewById(R.id.btnInstall);
			btnInstall.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					AppConnect.getInstance(Cuboc.this).showOffers(Cuboc.this);
			}});
	        SeekBar barMusic = (SeekBar)view.findViewById(R.id.barMusic);
	        barMusic.setProgress((int)(Settings.musicVolume*100));
	        barMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					Settings.musicVolume = seekBar.getProgress()/100f;
					Assests.backgroundMusics[0].setVolume(Settings.musicVolume);
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
	        barSound.setProgress((int)(Settings.soundVolume*100));
	        barSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					Settings.soundVolume = seekBar.getProgress()/100f;
				}				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	
				}				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
				}
			});
	        
			return new AlertDialog.Builder(Cuboc.this)
			.setView(view)
//			.setIcon(android.R.drawable.ic_menu_preferences)
//			.setTitle(getResources().getString(R.string.system_setting))
			.show();			
		}
		return super.onCreateDialog(id);
	}

}