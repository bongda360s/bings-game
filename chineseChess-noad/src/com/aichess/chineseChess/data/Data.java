package com.aichess.chineseChess.data;

import com.aichess.chineseChess.R;

/**
 * The Class Data.
 */
public class Data {
	public static final int[] SOUND_ID = { R.raw.click, R.raw.illegal, R.raw.move,
			R.raw.move2, R.raw.capture, R.raw.capture2, R.raw.check,
			R.raw.check2, R.raw.win, R.raw.draw, R.raw.loss};	
	public static final int RESP_CLICK = 0;
	public static final int RESP_ILLEGAL = 1;
	public static final int RESP_MOVE = 2;
	public static final int RESP_MOVE2 = 3;
	public static final int RESP_CAPTURE = 4;
	public static final int RESP_CAPTURE2 = 5;
	public static final int RESP_CHECK = 6;
	public static final int RESP_CHECK2 = 7;
	public static final int RESP_WIN = 8;
	public static final int RESP_DRAW = 9;
	public static final int RESP_LOSS = 10;	
	/** The Constant LOW_SCREEN_WIDTH. */
	public static final int LOW_SCREEN_WIDTH = 240;
	/** The Constant RS_DATA_LEN. */
	public static final int RS_DATA_LEN = 512;

	/** Handler 发送消息. */
	public static final int WHAT_THINKING = 0;

	/** The Constant WHAT_OTHER. */
	public static final int WHAT_OTHER = 1;
	
	public static final int WHAT_SOUND = 2;

	/**
	 * 0: 游戏状态, 0为正常退出, 1为游戏已保存<br>
	 * 16: 谁先走棋, 0为红先, 1为黑先 (即Flipped)<br>
	 * 17: 让子级别, 0为不让, 1为左马, 2为让双马, 3为让九子<br>
	 * 18: 电脑级别, 0为初级, 1为中级, 2为专家级<br>
	 * 19: 声音等级，0为静音，5为最大值<br>
	 * 20: 音乐等级，0为静音，5为最大值<br>
	 * 256-511: 棋盘.
	 */
	public static byte[] rsData = new byte[RS_DATA_LEN];

	/** 是否电脑先走，true电脑先走，false我先走. */
	public static boolean flipped;

	/** 让子级别 ：0为不让, 1为左马, 2为让双马, 3为让九子. */
	public static int handicap = 0;

	/** 电脑级别: 0为初级, 1为中级, 2为专家级. */
	public static int level = 0;

	/**
	 * Re init.
	 */
	public static final void reInit() {
		rsData = new byte[RS_DATA_LEN];
	}
	public static int Amount = 0;
	public static double Price = 0;
}
