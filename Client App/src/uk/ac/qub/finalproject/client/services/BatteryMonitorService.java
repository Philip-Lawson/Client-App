/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import uk.ac.qub.finalproject.s40143289.client.views.BatteryLevelBroadcastReceiver;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * This service monitors the battery status of the device using a battery level
 * broadcast receiver. This service was written as a simple way to circumvent
 * the restrictions on broadcast receivers that listen to changes in the
 * battery. <br>
 * </br> It provides an easy way to register and unregister the battery
 * broadcasting service without needing any knowledge of the broadcast
 * receiver's implementation. As all active background services appear in the
 * app status in the Android application settings, this gives the user an easy
 * way to tell if the app is currently monitoring the battery status.
 * 
 * @author Phil
 *
 */
public class BatteryMonitorService extends Service {

	/**
	 * THe broadcast receiver that is used to monitor and respond to changes in
	 * the battery status.
	 */
	private static final BroadcastReceiver batteryReceiver = new BatteryLevelBroadcastReceiver();

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryReceiver, intentFilter);	
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {		
		unregisterReceiver(batteryReceiver);
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
