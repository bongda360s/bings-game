package com.youmi;

import net.youmi.android.AdManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class HelloYoumi extends Activity {
	
	  static{ 
	    	AdManager.init("3cc0b18985648cb0", "6db73c99cfc2add4", 31, false,"2.1");   
	    }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutView());
    }
    
    View layoutView()
    {
    	LinearLayout layout=new LinearLayout(this);
    	layout.setOrientation(LinearLayout.VERTICAL);
    	
    	
    	
    	Button btn1=new Button(this);
    	btn1.setText("xml布局");
    	btn1.setOnClickListener(new Button.OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			try{
    			Intent intent=new Intent(HelloYoumi.this,XmlSample.class);
    			
    			startActivity(intent);
    			}catch (Exception e) {
					// TODO: handle exception
    				Log.e("HelloYoumi", e.getMessage());
				}
    		}
    	});
    	
    	
    	layout.addView(btn1);
    	
    	
    	Button btn2=new Button(this);
    	btn2.setText("代码布局");
    	btn2.setOnClickListener(new Button.OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			try{
    			Intent intent=new Intent(HelloYoumi.this,CodeSample.class);
    			startActivity(intent);
    			}catch (Exception e) {
					// TODO: handle exception
    				Log.e("HelloYoumi", e.getMessage());
				}
    			
    			String url="http://umlife1.gicp.net/sdk/images/h.jpg";
    		
    			
    		}
    	});
    	layout.addView(btn2);
    	
    	
    	Button btn3=new Button(this);
    	btn3.setText("悬浮布局");
    	btn3.setOnClickListener(new Button.OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			try{
    			Intent intent=new Intent(HelloYoumi.this,SuspensoidSample.class);
    			startActivity(intent);
    			}catch (Exception e) {
					// TODO: handle exception
    				Log.e("HelloYoumi", e.getMessage());
				}
    		}
    	});
    	layout.addView(btn3); 
    	
    	return layout;
    }
}