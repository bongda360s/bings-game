package com.aichess.jumpjump;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AndroidFiles;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.badlogic.gdx.backends.android.AndroidInput;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;

import com.badlogicgames.superjumper.Bob;
import com.badlogicgames.superjumper.Fighting;
import com.badlogicgames.superjumper.Settings;
import com.badlogicgames.superjumper.Settings.ADStatus;
import com.badlogicgames.superjumper.SuperJumper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waps.AdView;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;

public class SuperJumperAndroid extends AndroidApplication  implements UpdatePointsNotifier{
	
//	static{ 
//    	AdManager.init("3cc0b18985648cb0", "6db73c99cfc2add4", 31, false,"1.8");   
//    }
	private final int settingID = 1;
	private final int bulletinID = 2;
	View bulletinDialogView;
	int highestScore;
	
	
	//AdView adView;
	// Declare your APID, given to you by Millennial Media
	public final static String MYAPID = "43914";
	public final static String MYGOALID = "12345";
	
	// The ad view object
	//private MMAdView adView;
	boolean isNewAd = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		Settings.setPhoneName(telephonyManager.getDeviceId());
        
        FrameLayout frameLayout = new FrameLayout(this);       
        FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);               
        mainLayoutParams.gravity = Gravity.CENTER;
        frameLayout.setLayoutParams(mainLayoutParams);
        
        View view = initializeForView(new SuperJumper(), false);        
        view.setLayoutParams(createLayoutParams());
        frameLayout.addView(view);
        
        //waps ad
		AppConnect.getInstance(this);
		LinearLayout.LayoutParams holderParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		holderParams.gravity = Gravity.RIGHT;		
		LinearLayout holder = new LinearLayout(SuperJumperAndroid.this);		
				
		LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		containerParams.gravity = Gravity.RIGHT;
		LinearLayout container = new LinearLayout(SuperJumperAndroid.this);
		holder.addView(container,containerParams);
		new AdView(this,container).DisplayAd(30);//每30秒轮换一次广告，此参数可修改
		frameLayout.addView(holder,holderParams);
		getWapsPoints();
        //adview
        /*
        AdViewLayout adViewLayout = new AdViewLayout(this, "SDK20112311470452fuch0w1ffdtssz7");
        FrameLayout.LayoutParams adviewLayoutParams = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
        adviewLayoutParams.gravity = Gravity.TOP;
        frameLayout.addView(adViewLayout, adviewLayoutParams);                
        adViewLayout.invalidate();
        */
      //mm ad		
		// Create the adview
        /*
		adView = new MMAdView(this, MYAPID, MMAdView.BANNER_AD_TOP, 30);
		adView.setId(MMAdViewSDK.DEFAULT_VIEWID);
		FrameLayout.LayoutParams adLayoutParams = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        adLayoutParams.gravity = Gravity.LEFT;
        adView.setLayoutParams(adLayoutParams);	
		// Add the adview to the view layout
		frameLayout.addView(adView);
		
		// (Optional/Recommended) Set meta data (will be applied to subsequent ad requests)
//		Hashtable<String, String> metaData = GdxInvadersAndroid.createMetaData();
//		metaData.put("height", "53");
//		metaData.put("width", "320");
//		adView.setMetaValues(metaData);

		// (Optional) Set the listener to receive events about the adview
		adView.setListener(this);
		
		// (Optional) Start conversion tracking
		MMAdView.startConversionTrackerWithGoalId(this, MYGOALID);
		adView.callForAd();
        */
        /*
        Button btnNew = new Button(this);
        btnNew.setText("new button");
        FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);      
        btnParams.gravity = Gravity.BOTTOM;
        btnNew.setLayoutParams(btnParams);
        frameLayout.addView(btnNew);       
        */
        //youmi ad
        /*
        adView = new AdView(this,Color.GRAY, Color.RED, 100);
		adView.setAdListener(this);
		FrameLayout.LayoutParams adViewParams = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);      
		adViewParams.gravity = Gravity.TOP;
		adView.setLayoutParams(adViewParams);
        frameLayout.addView(adView);
        
        setContentView(frameLayout);
        */
        
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
        
        
        // Lookup your LinearLayout assuming it锟斤拷s been given
        // the attribute android:id="@+id/mainLayout"
        //LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        
        this.getWindow().addContentView(adView, createLayoutParams());
        // Add the adView to it
        //layout.addView(adView);
        // Initiate a generic request to load it with an ad
        adView.loadAd(new AdRequest());
        */
      //settings view        
        final ImageView settingsView = new ImageView(getApplicationContext());
        settingsView.setImageResource(android.R.drawable.ic_menu_preferences);
        FrameLayout.LayoutParams settingsParams = new FrameLayout.LayoutParams(72, 72);
        settingsParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        settingsView.setLayoutParams(settingsParams);
        settingsParams.setMargins(70, 0, 0, 7);
        settingsView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				//Settings.setStatus(0);
				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
				settingsView.startAnimation(animation);
				showDialog(settingID);
			}
		});        
        frameLayout.addView(settingsView);
        //bulletin view
        final ImageView bulletinView = new ImageView(getApplicationContext());
        bulletinView.setImageResource(android.R.drawable.ic_menu_sort_by_size);
        FrameLayout.LayoutParams bulletinParams = new FrameLayout.LayoutParams(72, 72);
        bulletinParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        bulletinParams.setMargins(0, 0, 70, 7);
        bulletinView.setLayoutParams(bulletinParams);
        bulletinView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				//Settings.setStatus(0);
				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
				bulletinView.startAnimation(animation);
				LayoutInflater inflater = getLayoutInflater();
				bulletinDialogView = inflater.inflate(R.layout.fightings, null);
		        setUnsubmitedText();
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
//			Spinner spnAD = (Spinner)view.findViewById(R.id.spnAD);
//			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SuperJumperAndroid.this, R.array.adChoice, android.R.layout.simple_spinner_item);
//			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//			spnAD.setAdapter(adapter);
//			int ad = 0;
//			if(Settings.adStatus==ADStatus.AddRobotech)
//            	ad=1;
//            else if(Settings.adStatus==ADStatus.HideAd)
//            	ad=2;
//			spnAD.setSelection(ad);
//			spnAD.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//				@Override
//				public void onItemSelected(AdapterView<?> parent, View view,
//						int position, long id) {
//					adView.setVisibility(View.VISIBLE);
//					switch(position){
//					case 1:
//						Settings.adStatus = ADStatus.AddRobotech;
//						break;
//					case 2:
//						Settings.adStatus = ADStatus.HideAd;
//						break;
//					default:
//						Settings.adStatus = ADStatus.Nothing;
//					}					
//				}
//
//				@Override
//				public void onNothingSelected(AdapterView<?> parent) {					
//				}});
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
	        
	        Button btnAward = (Button)view.findViewById(R.id.btnAward);
			btnAward.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					AppConnect.getInstance(SuperJumperAndroid.this).showOffers(SuperJumperAndroid.this);					
				}});
			
			return new AlertDialog.Builder(SuperJumperAndroid.this)
			.setView(view)
			.setIcon(android.R.drawable.ic_menu_preferences)
			.setTitle(getResources().getString(R.string.system_setting))
			.show();			
		case bulletinID:	
			bindScoreListView();
			final AlertDialog dialog = new AlertDialog.Builder(SuperJumperAndroid.this)
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
						if("".equals(name))
							name = getResources().getString(R.string.unsung_hero);
						
						HttpClient client = new DefaultHttpClient();
						client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
						client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
					
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
					        	if(Settings.getPhoneName().equals(fighting.getPhoneName())){
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
						setUnsubmitedText();
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
	void setUnsubmitedText() {
		for(int i = 0, length = Settings.getFightings().size(); i < length; ++i){
        	Fighting fighting = Settings.getFightings().get(i);
        	if(Settings.getPhoneName().equals(fighting.getPhoneName())){
        		highestScore = fighting.getScore();
        		break;
        	}
        }
		final TextView txtTitle = (TextView)bulletinDialogView.findViewById(R.id.txtTitle);
		txtTitle.setText(String.format(getResources().getString(R.string.best_result), highestScore));
	}
	void bindScoreListView() {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
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
		} catch(Exception e){
			e.printStackTrace();
		}
		finally {
			client.getConnectionManager().shutdown();
		}				
		if(fightings == null || fightings.size() == 0)
			fightings = Settings.getFightings();
		ArrayList<HashMap<String, Object>> scores = new ArrayList<HashMap<String, Object>>();
		for (int i = 0,length = fightings.size(); i < length; i++) {
		    HashMap<String, Object> score = new HashMap<String, Object>();
		    score.put("img", R.drawable.icon);
		    Fighting fighting = fightings.get(i);
		    score.put("name", fighting.getName());
		    score.put("score", fighting.getScore());
		    scores.add(score);
		}
      
		SimpleAdapter saImageItems = new SimpleAdapter(getApplicationContext(),
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
		Settings.setStatus(0);
		new AlertDialog.Builder(SuperJumperAndroid.this)
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
	private void getWapsPoints() {
		AppConnect.getInstance(SuperJumperAndroid.this).getPoints(SuperJumperAndroid.this);
	}
	@Override
	protected void onResume() {
		getWapsPoints();		
		super.onResume();
	}
	@Override
    protected void onDestroy() {
	  AppConnect.getInstance(this).finalize();
      super.onDestroy();
    }
	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		Settings.wapsValue = arg1;
		Settings.awardCount  = Settings.wapsValue/Settings.jumperValue;
		
		if(Settings.awardCount > 0){
			Bob.lives += Settings.awardCount;
			Settings.wapsValue -= Settings.awardCount * Settings.jumperValue;
			AppConnect.getInstance(SuperJumperAndroid.this).spendPoints(Settings.awardCount * Settings.jumperValue, SuperJumperAndroid.this);
		}
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		
	}
	//MM ad
	/**
	public void MMAdReturned(MMAdView adview)
	{
		Log.i("SampleApp", "Millennial Ad View Success");
		isNewAd = true;
//		runOnUiThread(new Runnable() {
//			public void run()
//			{ setProgressBarIndeterminateVisibility(false); }
//		});
	}
	
	public void MMAdFailed(MMAdView adview)
	{
		Log.i("SampleApp", "Millennial Ad View Failed");
//		runOnUiThread(new Runnable() {
//			public void run()
//			{ setProgressBarIndeterminateVisibility(false); }
//		});
	}
	
	public void MMAdClickedToNewBrowser(MMAdView adview)
	{
		Log.i("SampleApp", "Millennial Ad clicked, new browser launched");
		Settings.setStatus(0);
        if(isNewAd){
        	if(Settings.adStatus==ADStatus.HideAd){
        		adView.setVisibility(View.GONE);
        	}else if(Settings.adStatus == ADStatus.AddRobotech){
        		Bob.lives++;
        	}
        }
    	isNewAd = false;
	}
	
	public void MMAdClickedToOverlay(MMAdView adview)
	{
		Log.i("SampleApp", "Millennial Ad Clicked to overlay");
	}
	
	public void MMAdOverlayLaunched(MMAdView adview)
	{
		Log.i("SampleApp", "Millennial Ad Overlay Launched");
	}
	
	public void MMAdRequestIsCaching(MMAdView adview)
	{
		Log.i("SampleApp", "Millennial Ad caching request" );
	}
	
	public void MMAdCachingCompleted(MMAdView adview, boolean success)
	{
		Log.i("SampleApp", "Millennial Ad caching completed successfully: " + success);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
        case 0:
			if(resultCode == RESULT_CANCELED)
			{
				Log.i("SampleApp", "Millennial Ad Overlay Closed");
			}	
		}
	}
	*/
}