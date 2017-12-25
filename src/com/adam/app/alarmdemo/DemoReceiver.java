package com.adam.app.alarmdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DemoReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		DemoUtil.Print(this, "[onReceive] enter");
		
		Intent it = new Intent();
		it.setAction(DemoUtil.ACTION);
		
		arg0.sendBroadcast(it);

		Toast.makeText(arg0, "alarm...", Toast.LENGTH_LONG).show();
	}

}

/*
 * ===========================================================================
 * 
 * Revision history
 * 
 * ===========================================================================
 */
