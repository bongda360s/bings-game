
package com.aichess.starwar;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import net.youmi.android.AdListener;
import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
	static{ 
    	AdManager.init("f67d5f8c4e102945", "46ffddb0a2f1cf4d", 31, false,"1.0");
    }
	private final int settingID = 1;
	private final int bulletinID = 2;
	int adCount = 5;
	private boolean bReceiveAD = false;
	View bulletinDialogView;
	int highestScore;	
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
		Settings.setPhoneName(telephonyManager.getDeviceId());
		adCount = Settings.getAdCount();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.setContentView(R.layout.main);        
        
        //main view
        FrameLayout frameLayout = new FrameLayout(this);       
        FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);               
        mainLayoutParams.gravity = Gravity.CENTER;
        frameLayout.setLayoutParams(mainLayoutParams);  

        View mainView = initializeForView(new GdxInvaders(), false);        
        mainView.setLayoutParams(createLayoutParams());
        frameLayout.addView(mainView);
        
        //youmi ad
        adView = new AdView(this,Color.GRAY, Color.WHITE, 100);
		adView.setAdListener(this);
		FrameLayout.LayoutParams adViewParams = new FrameLayout.LayoutParams(360, 38);      
		adViewParams.gravity = Gravity.RIGHT;
		adView.setLayoutParams(adViewParams);
		adView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				adCount--;
				if(adCount==0)
					adView.setVisibility(View.INVISIBLE);
			}
		});
        frameLayout.addView(adView);
        
        //settings view        
        final ImageView settingsView = new ImageView(GdxInvadersAndroid.this);
        settingsView.setImageResource(android.R.drawable.ic_menu_preferences);
        FrameLayout.LayoutParams settingsParams = new FrameLayout.LayoutParams(72, 72);
        settingsParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        settingsView.setLayoutParams(settingsParams);
        settingsParams.setMargins(10, 0, 0, 7);
        settingsView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(Settings.getStatus()==1)
					Settings.setStatus(0);
				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
				settingsView.startAnimation(animation);
				showDialog(settingID);
			}
		});        
        frameLayout.addView(settingsView);
        
        //bulletin view
        final ImageView bulletinView = new ImageView(GdxInvadersAndroid.this);
        bulletinView.setImageResource(android.R.drawable.ic_menu_sort_by_size);
        FrameLayout.LayoutParams bulletinParams = new FrameLayout.LayoutParams(72, 72);
        bulletinParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        bulletinParams.setMargins(0, 0, 10, 7);
        bulletinView.setLayoutParams(bulletinParams);
        bulletinView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
				bulletinView.startAnimation(animation);
				LayoutInflater inflater = getLayoutInflater();
				bulletinDialogView = inflater.inflate(R.layout.fightings, null);
		        for(int i = 0, length = Settings.getFightings().size(); i < length; ++i){
		        	Fighting fighting = Settings.getFightings().get(i);
		        	if(fighting.getPhoneName().equals(Settings.getPhoneName())){
		        		highestScore = fighting.getScore();
		        		break;
		        	}
		        }
				final TextView txtTitle = (TextView)bulletinDialogView.findViewById(R.id.txtTitle);
				txtTitle.setText(String.format(getResources().getString(R.string.best_result), highestScore));
				showDialog(bulletinID);
			}
		});
        frameLayout.addView(bulletinView);        
        setContentView(frameLayout);        
    }
    
    protected FrameLayout.LayoutParams createLayoutParams() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
        		android.view.ViewGroup.LayoutParams.FILL_PARENT, 
        		android.view.ViewGroup.LayoutParams.FILL_PARENT);
        layoutParams.gravity = Gravity.TOP;
        return layoutParams;
    }

	@Override
	protected Dialog onCreateDialog(int id) {	
		LayoutInflater inflater = getLayoutInflater();
		switch(id){		
		case settingID:
			View view = inflater.inflate(R.layout.settings, null);
	        SeekBar barAD = (SeekBar)view.findViewById(R.id.barAD);
	        barAD.setProgress(Settings.getAdCount()-1);
	        final TextView txtAD = (TextView)view.findViewById(R.id.txtAD);
			txtAD.setText(String.format(getResources().getString(R.string.ad_message), barAD.getProgress()+1));
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
					txtAD.setText(String.format(getResources().getString(R.string.ad_message), seekBar.getProgress()+1));
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
			.setIcon(android.R.drawable.ic_menu_preferences)
			.setTitle(getResources().getString(R.string.system_setting))
			.show();			
		case bulletinID:	
			bindScoreListView();
			final AlertDialog dialog = new AlertDialog.Builder(GdxInvadersAndroid.this)
			.setTitle(getResources().getString(R.string.leave_name))
	        .setView(bulletinDialogView)
			.show();
	        Window window = dialog.getWindow();
	        WindowManager.LayoutParams lp = window.getAttributes(); 
	        lp.alpha = 0.8f;
	        window.setAttributes(lp);
	        Button btnSubmit = (Button)bulletinDialogView.findViewById(R.id.btnSubmit);
	        final EditText editText = ((EditText)bulletinDialogView.findViewById(R.id.txtName));
			btnSubmit.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					if(highestScore > 0){
						String name = editText.getText().toString();
						if(name.equals(""))
							name = getResources().getString(R.string.unsung_hero);
						
						HttpClient client = new DefaultHttpClient();
						try {						
							String url = "http://androidgame.sinaapp.com/ws.php";
							HttpPost httpPost = new HttpPost(url);
							List <NameValuePair> params=new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("n", name));
							params.add(new BasicNameValuePair("s", Integer.toString(highestScore)));
							params.add(new BasicNameValuePair("pn", Settings.getPhoneName()));
							params.add(new BasicNameValuePair("a", Integer.toString(Settings.appNo)));
							httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
							HttpResponse response = client.execute(httpPost);
							response.getEntity();
							//remove the best score
							for(int i = 0, length = Settings.getFightings().size(); i < length; ++i){
					        	Fighting fighting = Settings.getFightings().get(i);
					        	if(fighting.getPhoneName().equals(Settings.getPhoneName())){
					        		Settings.getFightings().remove(i);
					        		break;
					        	}
					        }
						}catch (Exception e) {
						    e.printStackTrace();
						}finally{
							client.getConnectionManager().shutdown();
						}
						bindScoreListView();
						dialog.dismiss();
					}
				}});
			Button btnCancel = (Button)bulletinDialogView.findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}});	  
	        return dialog;
		}
		return super.onCreateDialog(id);
	}

	void bindScoreListView() {
		HttpClient client = new DefaultHttpClient();
		List<Fighting> fightings = null;
		try {
			String url = "http://androidgame.sinaapp.com/rs.php?a="+Settings.appNo;
			HttpGet httpGet = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = client.execute(httpGet,responseHandler);
		    if(!response.trim().equals("false")){
		    	Gson json = new Gson();
		    	Type listType = new TypeToken<List<Fighting>>() {}.getType();
		    	fightings = json.fromJson(response, listType);			    	
		    }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			client.getConnectionManager().shutdown();
		}				
		if(fightings == null || fightings.size() == 0)
			fightings = Settings.getFightings();
		ArrayList<HashMap<String, Object>> scores = new ArrayList<HashMap<String, Object>>();
		for (int i = 0,length = fightings.size(); i < length && i < 20; i++) {
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
		final ListView listResult = (ListView) bulletinDialogView.findViewById(R.id.lstFighting);	        
		listResult.setAdapter(saImageItems);
//		for(int i = 0,length = listResult.getCount(); i < length; ++i){
//			if(fightings.get(i).getPhoneName().equals(Settings.getPhoneName())){
//				View childView = listResult.getChildAt(i);listResult.get
//				TextView txtName = (TextView)childView.findViewById(R.id.name);
//				txtName.setTextColor(Color.YELLOW);
//				TextView txtScore = (TextView)childView.findViewById(R.id.score);
//				txtScore.setTextColor(Color.YELLOW);
//			}
//		}
	}
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(GdxInvadersAndroid.this)
		.setTitle(getResources().getString(R.string.cancel_title))
		.setMessage(getResources().getString(R.string.cancel_confirm))
		.setIcon(android.R.drawable.ic_menu_close_clear_cancel)
		.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}})
			.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Settings.save();
				dialog.dismiss();
				System.runFinalizersOnExit(true);
				System.exit(0);				
			}})
			.show();
	}
}