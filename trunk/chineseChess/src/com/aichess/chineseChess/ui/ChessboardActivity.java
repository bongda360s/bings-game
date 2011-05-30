package com.aichess.chineseChess.ui;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.aichess.chineseChess.R;
import com.aichess.chineseChess.data.Data;
import com.aichess.chineseChess.data.DbHelper;
import com.aichess.chineseChess.utils.Debug;
import com.waps.AdView;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;

/**
 * The Class ChessboardActivity.
 */
public class ChessboardActivity extends Activity implements UpdatePointsNotifier{
	
@Override
	protected void onResume() {
		AppConnect.getInstance(ChessboardActivity.this).getPoints(ChessboardActivity.this);
		super.onResume();
	}

	//	static{
//		AdManager.setTest(false);
//		AdManager.setPublisherKey("1lbylp1a55p6p13utceuikkkndaurxu3jr1vyd1o1lsgt2qtyas1lpwd3b496i3d");
//		//OfferManager.setApplicationKey("1a2s354gyuuksjdt04qit0ceg13xvsfiwcodmyhkg6pt8d9dnr12r3lp9m6mkr3");
//		}
	@Override
	protected void onDestroy() {
		mp.stop();
		AppConnect.getInstance(this).finalize();
		super.onDestroy();
	}
	/** The m board view. */
	ChessboadView mBoardView;

	/** The m text. */
	TextView mInfoTextView;

	/** The layout. */
	FrameLayout mLayout;

	/** The m helper. */
	DbHelper mHelper;

	SoundPool mPool;
	MediaPlayer mp;
	boolean mHasSound = true;
	boolean mHasMusic = true;
	HashMap<Integer, Integer> mSoundsMap = new HashMap<Integer, Integer>();
	//TinmooConnect tinmooConnect;
	//TinmooOffer offer;
	Intent musicIntent;
	private int amount;
	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mLayout = (FrameLayout) LayoutInflater.from(this).inflate(
				R.layout.game_board_layout, null);
		setContentView(mLayout);
		
		//waps ad
		AppConnect.getInstance(this);		
		LinearLayout container =(LinearLayout)findViewById(R.id.AdLinearLayout);
		new AdView(this,container).DisplayAd(30);//每30秒轮换一次广告，此参数可修改
		AppConnect.getInstance(ChessboardActivity.this).getPoints(ChessboardActivity.this);
		//tinmoo
		/*
		AdView ad = (AdView) mLayout.findViewById(R.id.ad);
		ad.setAdListener(new AdListener(){
		@Override
		public void onFailedToReceiveAd(AdView adview) {
			System.out.println("onFailedToReceiveAd");
		}
		@Override
		public void onFailedToReceiveRefreshedAd(AdView adview) {
			System.out.println("onFailedToReceiveRefreshedAd");
		}
		@Override
		public void onReceiveAd(AdView adview) {
			System.out.println("onReceiveAd");
		}
		@Override
		public void onReceiveRefreshedAd(AdView adview) {
			System.out.println("onReceiveRefreshedAd");
		}
		});
		ad.requestFreshAd();
		*/
		//adview.cn
		/*
		AdViewLayout adViewLayout = new AdViewLayout(this, "SDK20110128120537w03x1azumjdq1ip");
        FrameLayout.LayoutParams adviewLayoutParams = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
        adviewLayoutParams.gravity = Gravity.TOP;
        mLayout.addView(adViewLayout, adviewLayoutParams);                
        adViewLayout.invalidate();
        */

		mp = MediaPlayer.create(this,R.raw.bgsound);
		mp.setLooping(true);	
		mPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		mHasSound = getSharedPreferences("data", 0).getBoolean("sound", true);
		mHasMusic = getSharedPreferences("data", 0).getBoolean("music", true);
		amount = getSharedPreferences("data", 0).getInt("amount", 100);
		// 如果设置为有声音，则初始化。
		if (mHasSound) {
			initSounds();
		}
		if(mHasMusic){
			mp.start();
		}
		loadConfigData();
		mHelper = new DbHelper(this);
		loadSavedGameData();
		//mInfoTextView = (TextView) findViewById(R.id.text);
		mBoardView = new ChessboadView(this, handler);
		Bitmap mBoardBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.board);
		FrameLayout.LayoutParams boardParams = new FrameLayout.LayoutParams(mBoardBitmap.getWidth(),mBoardBitmap.getHeight());
		boardParams.gravity = Gravity.CENTER;
		mBoardView.setLayoutParams(boardParams);
		mLayout.addView(mBoardView);
		
		mInfoTextView = new TextView(ChessboardActivity.this);	
		mInfoTextView.setText(R.string.info_please);
		mInfoTextView.setTextColor(0xffff00ff);
		FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		textParams.gravity = Gravity.CENTER;
		mInfoTextView.setLayoutParams(textParams);
		mLayout.addView(mInfoTextView);
		
		initPieceLayout();
		
		Button btnSetting = (Button)mLayout.findViewById(R.id.btn_setting);
		btnSetting.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				AlertDialog.Builder b = new AlertDialog.Builder(
						ChessboardActivity.this);
				b.setTitle(R.string.text_setting);
				LinearLayout setting = (LinearLayout) LayoutInflater.from(
						ChessboardActivity.this).inflate(
						R.layout.setting_layout, null);
				final SharedPreferences data = getSharedPreferences("data", 0);
				final Editor editor = data.edit();

				RadioGroup who = (RadioGroup) setting
						.findViewById(R.id.group_who);
				int w = data.getInt("who", 0);
				who.getChildAt(0).setId(0);
				who.getChildAt(1).setId(1);
				RadioButton wrb = (RadioButton) who.getChildAt(w);
				wrb.setChecked(true);
				who.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						editor.putInt("who", checkedId);
						editor.commit();
						Debug.e(checkedId + " ID");
					}
				});

				RadioGroup handicap = (RadioGroup) setting
						.findViewById(R.id.group_handicap);
				int h = data.getInt("handicap", 0);
				handicap.getChildAt(0).setId(0);
				handicap.getChildAt(1).setId(1);
				handicap.getChildAt(2).setId(2);
				handicap.getChildAt(3).setId(3);
				RadioButton hrb = (RadioButton) handicap.getChildAt(h);
				hrb.setChecked(true);
				handicap.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						editor.putInt("handicap", checkedId);
						Debug.e(checkedId + " ID");
						editor.commit();
					}
				});

				RadioGroup level = (RadioGroup) setting
						.findViewById(R.id.group_level);
				int l = data.getInt("level", 0);
				level.getChildAt(0).setId(0);
				level.getChildAt(1).setId(1);
				level.getChildAt(2).setId(2);
				RadioButton lrb = (RadioButton) level.getChildAt(l);
				lrb.setChecked(true);
				level.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						editor.putInt("level", checkedId);
						editor.commit();
					}
				});
				
				CheckBox music = (CheckBox) setting
				.findViewById(R.id.music);
				boolean m = data.getBoolean("music", true);
				music.setChecked(m);
				music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						editor.putBoolean("music", isChecked);
						editor.commit();
						mHasSound = isChecked;
						if(isChecked && !mp.isPlaying()){
							mp.start();
						}else if(mp.isPlaying()){
							mp.pause();
						}
					}
				});
				
				CheckBox sound = (CheckBox) setting
						.findViewById(R.id.sound);
				boolean s = data.getBoolean("sound", true);
				sound.setChecked(s);
				sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						editor.putBoolean("sound", isChecked);
						editor.commit();
						mHasSound = isChecked;
					}
				});
				TextView txtAmount = (TextView)setting.findViewById(R.id.txt_amount);
				txtAmount.setText(String.format(getResources().getString(R.string.text_nodescription,amount)));
				b.setView(setting);
				b.create().show();
			}});
		Button btnReset = (Button)mLayout.findViewById(R.id.btn_reset);
		btnReset.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				if (mBoardView.phase == ChessboadView.PHASE_THINKING) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.text_computerthink),
							Toast.LENGTH_LONG).show();
				}
				rePlay();
			}});
		Button btnBack = (Button)mLayout.findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {				
				if(amount > 0){
					if(mBoardView.retract()){
						AppConnect.getInstance(ChessboardActivity.this).spendPoints(10, ChessboardActivity.this);
						amount = amount - 10;
					}
				}else{
					new AlertDialog.Builder(ChessboardActivity.this)
					.setTitle(getResources().getString(R.string.text_nottitle))
					.setMessage(String.format(getResources().getString(R.string.text_nodescription,amount)))
					.setIcon(android.R.drawable.ic_menu_close_clear_cancel)
					.setNegativeButton(getResources().getString(R.string.text_back), new OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}})
						.setPositiveButton(getResources().getString(R.string.text_know), new OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							AppConnect.getInstance(ChessboardActivity.this).showOffers(ChessboardActivity.this);
						}})
						.show();
				}
			}});
		Button btnExit = (Button)mLayout.findViewById(R.id.btn_exit);
		btnExit.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				AppConnect.getInstance(ChessboardActivity.this).showOffers(ChessboardActivity.this);
			}});
	}

	void initSounds() {
		for (int i = 0; i < Data.SOUND_ID.length; i++) {
			mSoundsMap.put(i, mPool.load(this, Data.SOUND_ID[i], 1));
		}
	}
	
	void initMusic(){
		musicIntent = new Intent("com.angel.Android.MUSIC");
	    startService(musicIntent);	    
	}
	void stopMusic(){
		stopService(musicIntent);
	}
	
	void playSound(int response) {
		if (mHasSound) {
			mPool.play(mSoundsMap.get(response), 1.0f, 1.0f, 1, 0, 1.0f);
		}
	}

	/**
	 * Load data.
	 */
	void loadConfigData() {
		SharedPreferences data = getSharedPreferences("data", 0);
		int w = data.getInt("who", 0);
		int h = data.getInt("handicap", 0);
		int l = data.getInt("level", 0);
		Data.rsData[16] = (byte) w;
		Data.rsData[17] = (byte) h;
		Data.rsData[18] = (byte) l;
	}

	/**
	 * Load saved game data.
	 */
	void loadSavedGameData() {
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor c = db.query(DbHelper.TABLE_NAME, null, null, null, null, null,
				null);
		if (c.moveToFirst()) {
			byte[] data = c.getBlob(c.getColumnIndex(DbHelper._DATA));
			Data.rsData = data;
		}
		c.close();
		db.close();
	}

	/** The handler. */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Debug.e("Text " + mInfoTextView.getVisibility() + " "
					+ mInfoTextView.getText());
			super.handleMessage(msg);
			switch (msg.what) {
			case Data.WHAT_THINKING:
				mInfoTextView.setText(getResources().getString(
						R.string.info_thinking));
				break;
			case Data.WHAT_SOUND:
				playSound(msg.arg1);
				if (msg.obj != null) {
					mInfoTextView.setText((String) msg.obj);
				}
			case Data.WHAT_OTHER:
				mInfoTextView.setText((String) msg.obj);
				break;
			}
		}
	};

	/**
	 * Inits the piece layout.
	 */
	private void initPieceLayout() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		Debug.i("Screen size: " + width + "x" + height);
		checkScreen(width);
	}

	/**
	 * Check screen.
	 * 
	 * @param width
	 *            the width
	 */
	private void checkScreen(int width) {
		if (width < Data.LOW_SCREEN_WIDTH) {
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle(R.string.info_title);
			b.setMessage(R.string.info_not_support);
			b.setPositiveButton(R.string.text_positive_button,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			b.create().show();
		}
	}

	/**
	 * Re play.
	 */
	private void rePlay() {
		Data.reInit();
		loadConfigData();
		mBoardView.init();
		mBoardView.load();
		mBoardView.paint();
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {			
			onProgramExit();
			return true;
		}
		return false;
	}
	
	private void onProgramExit(){
		new AlertDialog.Builder(ChessboardActivity.this)
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
				//程序退出时，把金币数保存
				final SharedPreferences data = getSharedPreferences("data", 0);
				final Editor editor = data.edit();
				editor.putInt("amount", amount);
				SQLiteDatabase db = mHelper.getWritableDatabase();
				Cursor c = db.query(DbHelper.TABLE_NAME, null, null, null,
						null, null, null);
				Data.rsData[16] = (byte) (Data.flipped ? 1 : 0);
				Data.rsData[17] = (byte) Data.handicap;
				Data.rsData[18] = (byte) Data.level;
				ContentValues values = new ContentValues();
				values.put(DbHelper._DATA, Data.rsData);
				if (c.moveToFirst()) {
					// 如果数据存在，就更新
					db.update(DbHelper.TABLE_NAME, values, null, null);
				} else {
					// 数据不存在，就插入
					db.insert(DbHelper.TABLE_NAME, null, values);
				}
				c.close();
				db.close();
				finish();
				System.exit(0);			
			}})
			.show();
	}

	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		// TODO Auto-generated method stub
		amount = arg1 <= 0? amount : arg1;
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}
}