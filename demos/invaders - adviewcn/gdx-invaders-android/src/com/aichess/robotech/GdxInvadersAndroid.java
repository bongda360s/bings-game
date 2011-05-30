package com.aichess.robotech;

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
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.aichess.robotech.R;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdxinvaders.GdxInvaders;
import com.badlogic.gdxinvaders.simulation.Fighting;
import com.badlogic.gdxinvaders.simulation.Invader;
import com.badlogic.gdxinvaders.simulation.Settings;
import com.badlogic.gdxinvaders.simulation.Ship;
import com.badlogic.gdxinvaders.simulation.Settings.ADStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GdxInvadersAndroid extends AndroidApplication{
	//static{ 
    	//net.youmi.android.AdManager.init("53456fcbcabb048d", "88f38b54dcd85147", 30, false,1.8);
    	//set test mode.
    	//com.ignitevision.android.ads.AdManager.setTest(false);
		//set your key to load test ads.
    	//com.ignitevision.android.ads.AdManager.setPublisherKey("6gm2ookyn0cwe1266t3hm9z51ot3zje1vaajcau6svxlbd4c1kjoze9qfjgr0");
    //}
//	static{
//		AdManager.setTest(false);
//		AdManager.setPublisherKey("6gm2ookyn0cwe1266t3hm9z51ot3zje1vaajcau6svxlbd4c1kjoze9qfjgr0");
//		}
	private final int settingID = 1;
	private final int bulletinID = 2;
	View bulletinDialogView;
	int highestScore;
	//boolean isNewAd = true;
	//GuoheAdLayout adLayout;	
	/** Called when the activity is first created. */
	@Override public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		Settings.setPhoneName(telephonyManager.getDeviceId());
		
		//adCount = Settings.getAdCount();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //main view
        FrameLayout frameLayout = new FrameLayout(this);       
        FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);               
        mainLayoutParams.gravity = Gravity.CENTER;
        frameLayout.setLayoutParams(mainLayoutParams);         
        
        View mainView = initializeForView(new GdxInvaders(), false);        
        mainView.setLayoutParams(createLayoutParams());
        frameLayout.addView(mainView);
        
        //youmi ad 
        /*
        AdView adView = new AdView(this,Color.GRAY, Color.WHITE, 100);
		FrameLayout.LayoutParams adViewParams = new FrameLayout.LayoutParams(420,LayoutParams.WRAP_CONTENT);
		adViewParams.gravity = Gravity.RIGHT;
		adView.setLayoutParams(adViewParams);	
        frameLayout.addView(adView); 
        */
        //guohe ad 
        /*
        adLayout = new GuoheAdLayout(GdxInvadersAndroid.this);
        FrameLayout.LayoutParams adLayoutParams = new FrameLayout.LayoutParams(420, LayoutParams.WRAP_CONTENT);
        adLayoutParams.gravity = Gravity.RIGHT;
        adLayout.setLayoutParams(adLayoutParams);
        adLayout.setListener(new GuoheAdStateListener() {
            @Override
            public void onFail() {
                Log.v("GuoheAd", "OnFail");
            }

            @Override
            public void onClick() {
            	Log.v("GuoheAd", "onclick");
            	Settings.setStatus(0);
                if(isNewAd){
                	if(Settings.adStatus==ADStatus.HideAd){
                		adLayout.setVisibility(View.GONE);
                	}else if(Settings.adStatus == ADStatus.AddRobotech){
                		Ship.lives++;
                	}
                }
            	isNewAd = false;
            }

            @Override
            public void onReceiveAd() {
            	Log.v("GuoheAd", "onreceive ad");
            	isNewAd = true;
            }

            @Override
            public void onRefreshAd() {
                Log.v("GuoheAd", "OnRefreshAd");
            }
        });
        frameLayout.addView(adLayout);
        */
        
        /*
        //wi ad
        com.wiyun.ad.AdView ad = new com.wiyun.ad.AdView(this);
		ad.setResId("53289bdbd732d521");
		FrameLayout.LayoutParams adLayoutParams = new FrameLayout.LayoutParams(400, LayoutParams.WRAP_CONTENT);
        adLayoutParams.gravity = Gravity.RIGHT;
        ad.setLayoutParams(adLayoutParams);
		frameLayout.addView(ad);
		ad.requestAd();
		ad.setRefreshInterval(30);
		*/
        /*
        //admob        
        final com.admob.android.ads.AdView adview = new com.admob.android.ads.AdView(getApplicationContext());
        FrameLayout.LayoutParams adLayoutParams = new FrameLayout.LayoutParams(400, LayoutParams.WRAP_CONTENT);
        adLayoutParams.gravity = Gravity.RIGHT;
        adview.setLayoutParams(adLayoutParams);
        adview.setBackgroundColor(0x777777);
        adview.setTextColor(0x000099);
        adview.setRequestInterval(15);
        //adview.setAlwaysDrawnWithCacheEnabled(true);
        adview.setListener(new com.admob.android.ads.AdView.AdListener()
        {
			public void onFailedToReceiveAd(AdView adView) {
			}

			public void onNewAd() {				
			}

			@Override
			public void onReceiveAd(com.admob.android.ads.AdView adView) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailedToReceiveAd(com.admob.android.ads.AdView adView) {
				// TODO Auto-generated method stub
				
			}        	
        });
        adview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Settings.setStatus(0);
			}
		});
        frameLayout.addView(adview); 
        */
        /*
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.resbackground);
        imageView.setClickable(true);
        //imageView.setAlpha(0);
        FrameLayout.LayoutParams imageLayoutParams = new FrameLayout.LayoutParams(400, 20);
        imageLayoutParams.gravity = Gravity.RIGHT;
        imageView.setLayoutParams(adLayoutParams);
        imageView.setScaleType(ScaleType.CENTER_INSIDE);
        imageView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("you click ad");
				if(isNewAd){
					Settings.setClickNewAd(true);
					isNewAd = false;
					adview.performClick();
				}
				return true;
			}
		});
        frameLayout.addView(imageView);
        */ 
        
        
		//tinmoo ad
        /*
        com.ignitevision.android.ads.AdView tinmooview = new com.ignitevision.android.ads.AdView(getApplicationContext());
        FrameLayout.LayoutParams tinmooLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        tinmooLayoutParams.gravity = Gravity.RIGHT;
        tinmooview.setLayoutParams(tinmooLayoutParams);
        tinmooview.setRequestInterval(20);
        tinmooview.setAdListener(new SimpleAdListener(){
			public void onFailedToReceiveAd(AdView adview) {
				// TODO Auto-generated method stub
				// do something here when ads fail to received.
				// if no ads or connect fail, this method will be callback. 
				Log.i("sdk", "fail to received");
			}

			public void onReceiveAd(AdView adview) {
				// TODO Auto-generated method stub
				// do something here when ads have received.
				//adview.setBackgroundColor(Color.BLACK);
				//adview.setTextColor(Color.WHITE);
				//adview.setRequestInterval(20);
		        
				Log.i("sdk", "ads received");
			}

			public void onFailedToReceiveRefreshedAd(AdView arg0) {
				// TODO Auto-generated method stub
				Log.i("sdk", "ads failed to refresh an new ad");
			}

			public void onReceiveRefreshedAd(AdView arg0) {
				// TODO Auto-generated method stub
				Log.i("sdk", "ads refresh an new ad");
			}
        	
          });
        frameLayout.addView(tinmooview);
        */	
        /*
		tinmooview.setOnTouchListener(new View.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("on touch");
				return false;
			}});
        tinmooview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				System.out.println("focus change");
				
			}
		});
        tinmooview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("click");
				
			}
		});
        */
        //adview
        /*
        AdViewLayout adViewLayout = new AdViewLayout(this, "SDK20112216510558z5zom6e0izk6et4");
        FrameLayout.LayoutParams adviewLayoutParams = new FrameLayout.LayoutParams(420,LayoutParams.WRAP_CONTENT);
        adviewLayoutParams.gravity = Gravity.RIGHT;
        frameLayout.addView(adViewLayout, adviewLayoutParams);                
        adViewLayout.invalidate();
        */
        //casee
        /*
        CaseeAdView cav = new CaseeAdView( this, "C791C3E05B5A6F7A9414B6199E4FA9C7",true,
				30*1000,Color.BLACK, Color.WHITE,false);
        FrameLayout.LayoutParams caseeLayoutParams = new FrameLayout.LayoutParams(420,LayoutParams.WRAP_CONTENT);
        caseeLayoutParams.gravity = Gravity.RIGHT;
        frameLayout.addView(cav, caseeLayoutParams); 
        */   
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
				Settings.setStatus(0);
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
			
			Spinner spnAD = (Spinner)view.findViewById(R.id.spnAD);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(GdxInvadersAndroid.this, R.array.adChoice, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spnAD.setAdapter(adapter);
			int ad = 0;
			if(Settings.adStatus==ADStatus.AddRobotech)
            	ad=1;
            else if(Settings.adStatus==ADStatus.HideAd)
            	ad=2;
			spnAD.setSelection(ad);
			spnAD.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					//adLayout.setVisibility(View.VISIBLE);
					switch(position){
					case 1:
						Settings.adStatus = ADStatus.AddRobotech;
						break;
					case 2:
						Settings.adStatus = ADStatus.HideAd;
						break;
					default:
						Settings.adStatus = ADStatus.Nothing;
					}					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {					
				}});
			
			
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
			//.setIcon(android.R.drawable.ic_menu_preferences)
			//.setTitle(getResources().getString(R.string.system_setting))
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
        	if(fighting.getPhoneName().equals(Settings.getPhoneName())){
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
		for (int i = 0,length = fightings.size(); i < length ; i++) {
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
		Settings.setStatus(0);
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
	
	@Override
    protected void onDestroy() {
      super.onDestroy();
      //GuoheAdManager.finish(this);
    }
}