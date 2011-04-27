
package com.aichess.starwar;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.youmi.android.AdListener;
import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdxinvaders.GdxInvaders;
import com.badlogic.gdxinvaders.simulation.Fighting;
import com.badlogic.gdxinvaders.simulation.Invader;
import com.badlogic.gdxinvaders.simulation.Settings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GdxInvadersAndroid extends AndroidApplication implements AdListener {
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(GdxInvadersAndroid.this)
		.setTitle("退出确认")
		.setMessage("您确定要退出星际捍卫者吗？")
		.setIcon(android.R.drawable.title_bar)
		.setNegativeButton("取消", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}})
			.setPositiveButton("确定", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Settings.save();
				System.runFinalizersOnExit(true);
				System.exit(0);				
			}})
			.show();
	}

	private final int settingID = 1;
	private final int bulletinID = 2;
	private int adCount = 5;
	private boolean bReceiveAD = false;
	private View view;
	static{ 
    	AdManager.init("f67d5f8c4e102945", "46ffddb0a2f1cf4d", 31, false,"1.0");
    }
	
	AdView adView;
	
	@Override
	public void onConnectFailed() {
		System.out.println("received faild");
	}

	@Override
	public void onReceiveAd() {
		bReceiveAD = true;
	}
	
	/** Called when the activity is first created. */
	@Override public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		Settings.phoneName = telephonyManager.getDeviceId();
		adCount = Settings.getAdCount();
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
        
        GdxInvaders invaders = new GdxInvaders();
//        invaders.setOnSubmitScoreListener(new GdxInvaders.OnSubmitScoreListener() {
//			
//			@Override
//			public void onSubmitListener(final int score) {				
//				new ShowDialogRunnable().run();				
//			}
//		});
        View view = initializeForView(invaders, false);        
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
				adCount--;
				if(adCount==0)
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
        bulletinView.setImageResource(R.drawable.settings);
        //bulletinView.setAlpha(180);
        FrameLayout.LayoutParams bulletinParams = new FrameLayout.LayoutParams(72, 72);
        bulletinParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        bulletinParams.setMargins(0, 0, 10, 7);
        bulletinView.setLayoutParams(bulletinParams);
        bulletinView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(bulletinID);
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
	        barAD.setProgress(Settings.getAdCount()-1);
	        final TextView txtAD = (TextView)view.findViewById(R.id.txtAD);
			txtAD.setText(String.format("激活%d个广告后，广告条消失", barAD.getProgress()+1));
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
					txtAD.setText(String.format("激活%d个广告后，广告条消失", seekBar.getProgress()+1));
					adCount = seekBar.getProgress()+1;
					Settings.setAdCount(adCount);
					adView.setVisibility(View.VISIBLE);
				}
			});
	        
	        SeekBar barMusic = (SeekBar)view.findViewById(R.id.barMusic);
	        barMusic.setProgress((int)(Settings.getMusicVolume()*100));
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
	        barSound.setProgress((int)(Settings.getSoundVolume()*100));
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
			InputStream in = null;
				
			try {
				String url = "http://androidgame.sinaapp.com/rs.php?a="+Settings.appNo;
			    URLConnection connection = new URL(url).openConnection();
			    connection.setConnectTimeout(1000 * 6); // 设置连接超时时间: 6s
			    connection.setReadTimeout(1000 * 6); // 设置读取超时时间: 6s
			    connection.connect();

			    in = connection.getInputStream();

			    // 获取HTTP响应结果
			    int length;
			    byte[] buffer = new byte[1024 * 4];
			    StringBuilder stringBuffer = new StringBuilder();
			    while ((length = in.read(buffer)) != -1) {
			        stringBuffer.append(new String(buffer, 0, length));
			    }
			    String response = stringBuffer.toString();
			    System.out.println(response);
			    if(!response.trim().equals("false")){
			    	Gson json = new Gson();
			    	Type listType = new TypeToken<List<Fighting>>() {}.getType();
			    	List<Fighting> fightings = json.fromJson(response, listType);
			    	
			    	Settings.setNetFightings(fightings);
			    }
			}
			catch (Exception e) {
			    // 出错处理代码...
			}
			finally {
			    // 关闭输入流
				try{
					in.close();
				}catch(Exception e){}
			}
			
			List<Fighting> fightings = Settings.getNetFightings();
			if(fightings == null || fightings.size() == 0)
				fightings = Settings.getFightings();
			ArrayList<HashMap<String, Object>> scores = new ArrayList<HashMap<String, Object>>();
	        for (int i = 0,length = fightings.size(); i < length && i < 5; i++) {
	        	if(fightings.get(i)==null)
	        		break;
	            HashMap<String, Object> score = new HashMap<String, Object>();
	            score.put("img", R.drawable.icon);
	            Fighting fighting = fightings.get(i);
	            score.put("name", fighting.getName());
	            score.put("score", fighting.getScore());
	            scores.add(score);
	        }
	       
	        SimpleAdapter saImageItems = new SimpleAdapter(GdxInvadersAndroid.this,
	        		scores,
	        		R.layout.score,
	                new String[] { "img", "name", "score" },
	                new int[] { R.id.img, R.id.name, R.id.score });
	        
	        view = inflater.inflate(R.layout.fightings, null);	        
	        ((ListView) view.findViewById(R.id.lstFighting)).setAdapter(saImageItems);
	        final AlertDialog dialog = new AlertDialog.Builder(GdxInvadersAndroid.this)
			.setView(view)
			.show();
	        Window window = dialog.getWindow();
	        WindowManager.LayoutParams lp = window.getAttributes(); 
	        lp.alpha = 0.8f;
	        window.setAttributes(lp);

			final int score = Settings.getFightings().get(0).getScore();
			TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
			txtTitle.setText("最好成绩是："+Integer.toString(score)+",请在英雄榜上留下您的大名");
			Button btnSubmit = (Button)view.findViewById(R.id.btnSubmit);
			btnSubmit.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					EditText txt = null;
					// TODO Auto-generated method stub
					String name = ((EditText)view.findViewById(R.id.txtName)).getText().toString();
					if(name.equals(""))
						name = "unknown hero";
					Settings.getFightings().get(0).setName(name);
					try {						
						String url = String.format("http://androidgame.sinaapp.com/ws.php?n=%s&s=%d&pn=%s&a=%d", name,score,Settings.phoneName,Settings.appNo);
					    URLConnection connection = new URL(url).openConnection();
					    connection.setConnectTimeout(1000 * 6); // 设置连接超时时间: 6s
					    connection.setReadTimeout(1000 * 6); // 设置读取超时时间: 6s
					    connection.connect();
					}
					catch (Exception e) {
					    // 出错处理代码...
					}
					dialog.dismiss();
					
				}});
			Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}});	        
	        return dialog;
		}
		return super.onCreateDialog(id);
	}
}