package com.youmi;

import net.youmi.android.AdListener;
import net.youmi.android.AdView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SuspensoidSample extends Activity implements AdListener{

	
	AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);View view=initView();
                
        setContentView(view);

       
		adView = new AdView(this,Color.GRAY, Color.WHITE, 100);
        
	

		adView.setPadding(0, 120, 0, 0);
		
		adView.setAdListener(this);
	 
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
 
		addContentView(adView, params);
		

	}
	
	
	 
	View initView() {
		
		
		this.setTitle("悬浮布局示例");
		
		LinearLayout layout=new LinearLayout(this);
		
		layout.setOrientation(LinearLayout.VERTICAL);
				
		layout.setBackgroundResource(R.drawable.bg);
	   
	
		return layout;	

	}
	 

	
	@Override
	public void onReceiveAd() {
		// TODO Auto-generated method stub
		 
		 
	}

	@Override
	public void onConnectFailed() {
		// TODO Auto-generated method stub
		 
	}
}
