package com.youmi;

import net.youmi.android.AdListener;
import net.youmi.android.AdView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CodeSample extends Activity implements AdListener {

	AdView adView;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view=initView();		
	    setContentView(view);
	    
	    
	} 
	View initView() { 
		this.setTitle("代码布局示例"); 
		LinearLayout layout=new LinearLayout(this); 
		layout.setOrientation(LinearLayout.VERTICAL); 
		layout.setBackgroundResource(R.drawable.bg); 
		adView = new AdView(this); 
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);		
		layout.addView(adView, params); 
		adView.setAdListener(this); 		
		return layout;
	}
	@Override
	public void onReceiveAd() {		 
	}

	@Override
	public void onConnectFailed() {
	}
}
