package com.aichess.chineseChess.utils;

import android.util.Log;

/**
 * The Class Debug.<br>
 * Shown debug information in Logcat.
 */
public class Debug {
	
	/** The Constant TAG. */
	public static final String TAG = "xqwdroid";
	
	/** Set if enable debug information. */
	private final static boolean DEBUG_ENABLE = false;

	/**
	 * Print information.
	 *
	 * @param msg The information to be printed.
	 */
	public static final void i(String msg) {
		if (DEBUG_ENABLE) {
			Log.i(TAG, msg);
		}
	}

	/**
	 * Print debug information.
	 *
	 * @param msg The information to be printed.
	 */
	public static final void d(String msg) {
		if (DEBUG_ENABLE) {
			Log.d(TAG, msg);
		}
	}

	/**
	 * Print Error information.
	 *
	 * @param msg The information to be printed.
	 */
	public static final void e(String msg) {
		if (DEBUG_ENABLE) {
			Log.e(TAG, msg);
		}
	}
}
