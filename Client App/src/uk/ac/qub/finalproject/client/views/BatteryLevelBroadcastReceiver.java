/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

import uk.ac.qub.finalproject.client.services.DataProcessingService;
import uk.ac.qub.finalproject.client.views.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;

/**
 * This broadcast receiver listens out for changes in the battery status and
 * starts or stops the data processing service according to the user's
 * preferences.
 * 
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

	/**
	 * Helper method returns true if the device is currently permitted to
	 * process data.
	 * 
	 * @param context
	 * @param intent
	 * @return
	 */
	private boolean canProcessData(Context context, Intent intent) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		return isCharging(intent, pref, context)
				|| aboveThreshold(intent, pref, context);
	}

	/**
	 * Helper method returns true if the battery is charging. Note that if the
	 * battery is charging and the user doesn't want the device to process data
	 * when the device is plugged in (regardless of battery level) this will
	 * return false.
	 * 
	 * @param batteryInfo
	 * @param pref
	 * @param context
	 * @return
	 */
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

	/**
	 * Helper method returns true if the battery level is above the user
	 * specified threshold for processing data.
	 * 
	 * @param batteryInfo
	 * @param pref
	 * @param context
	 * @return
	 */
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
