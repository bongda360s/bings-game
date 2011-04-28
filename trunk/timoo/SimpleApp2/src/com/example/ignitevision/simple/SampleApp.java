package com.example.ignitevision.simple;


import com.ignitevision.android.ads.AdManager;
import com.ignitevision.android.ads.AdView;
import com.ignitevision.android.ads.SimpleAdListener;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;



public class SampleApp extends Activity {
    
	static{
		// set test mode.
		AdManager.setTest(true);
		// set your key to load test ads.
		AdManager.setPublisherKey("1fh3l66kos2l71m8pht1wstoqq19es197axj8i5c4kwzshyczdz1n5yuhrzcuyz");
	}
	
	private AdView v;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //v = new AdView(this);
        v = (AdView) findViewById(R.id.ad);
        v.setAdListener(new SimpleAdListener(){

			@Override
			public void onFailedToReceiveAd(AdView adview) {
				// TODO Auto-generated method stub
				// do something here when ads fail to received.
				// if no ads or connect fail, this method will be callback. 
				Log.i("sdk", "fail to received");
			}

			@Override
			public void onReceiveAd(AdView adview) {
				// TODO Auto-generated method stub
				// do something here when ads have received.
				//adview.setBackgroundColor(Color.BLACK);
				//adview.setTextColor(Color.WHITE);
				//adview.setRequestInterval(20);
		        
				Log.i("sdk", "ads received");
			}

			@Override
			public void onFailedToReceiveRefreshedAd(AdView arg0) {
				// TODO Auto-generated method stub
				Log.i("sdk", "ads failed to refresh an new ad");
			}

			@Override
			public void onReceiveRefreshedAd(AdView arg0) {
				// TODO Auto-generated method stub
				Log.i("sdk", "ads refresh an new ad");
			}
        	
          });
        
        //addContentView(v, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));

    }
    
}