/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import uk.ac.qub.finalproject.client.views.BatteryLevelBroadcastReceiver;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * @author Phil
 *
 */
public class BatteryMonitorService extends Service {

	private static final BroadcastReceiver batteryReceiver = new BatteryLevelBroadcastReceiver();

	@Override
	public void onCreate() {
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		this.registerReceiver(batteryReceiver, intentFilter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		this.unregisterReceiver(batteryReceiver);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
