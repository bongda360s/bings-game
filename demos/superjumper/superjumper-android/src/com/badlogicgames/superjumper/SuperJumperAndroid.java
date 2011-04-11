package com.badlogicgames.superjumper;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogicgames.superjumper.SuperJumper;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class SuperJumperAndroid extends AndroidApplication {
	private String MY_AD_UNIT_ID = "a14da2f83bc217d";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initialize(new SuperJumper(), false);
        // Create the adView
        AdView adView = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);
        // Lookup your LinearLayout assuming it¡¯s been given
        // the attribute android:id="@+id/mainLayout"
        //LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        LayoutParams lps = new LayoutParams( 320, 60);
        this.getWindow().addContentView(adView, lps);
        // Add the adView to it
        //layout.addView(adView);
        // Initiate a generic request to load it with an ad
        adView.loadAd(new AdRequest());
    }
}