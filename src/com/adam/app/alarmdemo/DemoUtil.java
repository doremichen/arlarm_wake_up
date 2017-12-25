package com.adam.app.alarmdemo;

import android.util.Log;

public class DemoUtil {

	public static final String ACTION = "com.adam.action.screen.on";
	
	private static final String TAG = "Demo";
	
	public static void Print(Object obj, String str) {
		
		Log.i(TAG, obj.getClass().getSimpleName() + " " + str);
	}
}

/*
 * ===========================================================================
 * 
 * Revision history
 * 
 * ===========================================================================
 */
