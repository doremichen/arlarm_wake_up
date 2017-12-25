package com.adam.app.alarmdemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements UIObserve {

	private EditText mEditText;
	private UIReceiver mUIReceiver;

	// Create the UI reciever
	private class UIReceiver extends BroadcastReceiver {

		private ArrayList<UIObserve> mList = new ArrayList<UIObserve>();

		public void addObserve(UIObserve observe) {
			mList.add(observe);
		}

		public void removeObserve(UIObserve observe) {
			mList.remove(observe);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			DemoUtil.Print(this, "[onReceive] enter");
			String action = intent.getAction();

			if (action.equals(DemoUtil.ACTION)) {
				DemoUtil.Print(this, DemoUtil.ACTION);

				for (UIObserve observe : mList) {
					observe.update();
				}
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		DemoUtil.Print(this, "[onCreate] enter");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mEditText = (EditText) this.findViewById(R.id.editText1);

		// register ui receiver
		mUIReceiver = new UIReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DemoUtil.ACTION);
		this.registerReceiver(mUIReceiver, intentFilter);

		// add observe to the UI receiver
		mUIReceiver.addObserve(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DemoUtil.Print(this, "[onDestroy] enter");

		// remove observe to the UI receiver
		mUIReceiver.removeObserve(this);

		// unregister UI receiver
		this.unregisterReceiver(mUIReceiver);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onStartAlarm(View v) {

		DemoUtil.Print(this, "[onStartAlarm] enter");

		// Hide soft keyboard
		View view = this.getCurrentFocus();

		if (view != null) {
			InputMethodManager imm = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
		}

		String num = this.mEditText.getText().toString();

		if (num.equals("")) {

			Toast.makeText(this.getApplicationContext(),
					"Please input the valid number", Toast.LENGTH_LONG).show();
		} else {

			int value = Integer.parseInt(num);

			// config pending intent for alarm
			Intent it = new Intent(this, DemoReceiver.class);
			PendingIntent pendingIt = PendingIntent.getBroadcast(
					this.getApplicationContext(), 0, it,
					PendingIntent.FLAG_ONE_SHOT);

			// get alarm manager handler
			AlarmManager alarmMg = (AlarmManager) this
					.getSystemService(Context.ALARM_SERVICE);

			// config alarm and start alarm
			alarmMg.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
					+ (value * 1000L), pendingIt);

			// Show toast to notify the user.
			Toast.makeText(this.getApplicationContext(),
					"Alarm set in " + value + " seconds", Toast.LENGTH_LONG)
					.show();

		}

	}

	@Override
	public void update() {

		DemoUtil.Print(this, "[update] enter");

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass()
						.getName());
		wl.acquire(1000L);
	}
}
