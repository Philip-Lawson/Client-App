/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

import uk.ac.qub.finalproject.client.services.NetworkService;
import uk.ac.qub.finalproject.client.views.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * The network info receiver listens for changes in the network status. Should
 * the network become available it will check the log for any incomplete
 * networking tasks e.g. sending results or loading the latest processor class.
 * 
 * @author Phil
 *
 */
public class NetworkInfoReceiver extends BroadcastReceiver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String networkKey = context.getString(R.string.wifi_key);
		String wifiOnly = context.getString(R.string.network_description_wifi);

		if (isConnected(pref, networkKey, wifiOnly, networkInfo.getType())
				&& networkInfo.isConnected()) {
			Intent i = new Intent(context, NetworkService.class);
			context.startService(i);
		}

	}

	private boolean isConnected(SharedPreferences pref, String networkKey,
			String wifiOnly, int networkInfo) {

		if (pref.getString(networkKey, wifiOnly).equals(wifiOnly)) {
			return networkInfo == ConnectivityManager.TYPE_WIFI;
		} else {
			return networkInfo == ConnectivityManager.TYPE_WIFI
					|| networkInfo == ConnectivityManager.TYPE_MOBILE;
		}
	}

}
