package com.sombrero.huotari.redditcutepics.common;

import android.util.Log;

public class L {
	public static void d(String TAG, String message) {
		Log.d(TAG, message);
	}

	public static void e(String TAG, String message, Throwable t) {
		Log.e(TAG, message, t);
	}
}
