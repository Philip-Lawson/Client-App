/**
 * 
 */
package com.example.fgjkj;

import uk.ac.qub.finalproject.client.services.DataProcessingService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;

/**
 * @author Phil
 *
 */
public class BatteryLevelBroadcastReceiver extends BroadcastReceiver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		if (canProcessData(context, intent)) {
			context.startService(new Intent(context,
					DataProcessingService.class));
		} else {
			context.stopService(new Intent(context, DataProcessingService.class));
		}

	}

	private boolean canProcessData(Context context, Intent intent) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		return isCharging(intent, pref, context)
				|| aboveThreshold(intent, pref, context);
	}

	private boolean isCharging(Intent batteryInfo, SharedPreferences pref,
			Context context) {
		boolean chargingEnabled = pref.getBoolean(
				context.getString(R.string.charging_key), true);
		int batteryStatus = batteryInfo.getIntExtra(
				BatteryManager.EXTRA_STATUS, -1);
		boolean charging = batteryStatus == BatteryManager.BATTERY_PLUGGED_AC
				|| batteryStatus == BatteryManager.BATTERY_PLUGGED_USB;

		return chargingEnabled && charging;

	}

	private boolean aboveThreshold(Intent batteryInfo, SharedPreferences pref,
			Context context) {
		String chargingPref = pref.getString(
				context.getString(R.string.battery_limit_key), "0");
		int chargeLimit = Integer.parseInt(chargingPref);

		int level = batteryInfo.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		int scale = batteryInfo.getIntExtra(BatteryManager.EXTRA_SCALE, 0);

		double currentCharge = scale / (double) level;
		double percentageThreshold = chargeLimit / 100.0;

		return currentCharge > percentageThreshold;
	}

}
