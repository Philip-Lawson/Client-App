/**
 * 
 */
package uk.ac.qub.finalproject.s40143289.client.views;

import uk.ac.qub.finalproject.client.persistence.DataStorage;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import uk.ac.qub.finalproject.client.services.BatteryMonitorService;
import uk.ac.qub.finalproject.client.services.DataProcessingService;
import uk.ac.qub.finalproject.client.services.DormantService;
import uk.ac.qub.finalproject.client.services.NetworkService;
import uk.ac.qub.finalproject.client.services.SendResultsService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

/**
 * This runnable deletes all of the user's data, stops all services and
 * unregisters the network receiver. This runnable should only be started when
 * the user's account has been deleted on the server.
 * 
 * @author Phil
 *
 */
public class DeleteAccountRunnable implements Runnable {

	private Context context;

	/**
	 * The handler registered with the UI page that called this runnable. It
	 * will be called when the deletion is complete.
	 */
	private Handler handler;

	public DeleteAccountRunnable(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	/**
	 * Deletes all data from the shared preferences.
	 * 
	 * @param context
	 */
	private void deletePreferences(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		pref.edit().clear().commit();
	}

	/**
	 * Unregisters the network broadcast receiver.
	 * 
	 * @param context
	 */
	private void unregisterReceivers(Context context) {
		ComponentName networkInfoListener = new ComponentName(context,
				NetworkInfoReceiver.class);
		context.getPackageManager().setComponentEnabledSetting(
				networkInfoListener,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}

	/**
	 * Stops all application services.
	 * 
	 * @param context
	 */
	private void stopServices(Context context) {
		Class<?>[] services = { BatteryMonitorService.class,
				DataProcessingService.class, DormantService.class,
				NetworkService.class, SendResultsService.class };

		for (Class<?> service : services) {
			Intent intent = new Intent(context, service);
			context.stopService(intent);
		}
	}

	/**
	 * Deletes all data from the persistence layer.
	 * 
	 * @param context
	 */
	private void deleteData(Context context) {
		DataStorage persistence = FileAndPrefStorage
				.getInstance(context);
		persistence.deleteAllData();
	}

	@Override
	public void run() {
		deletePreferences(context);
		unregisterReceivers(context);
		stopServices(context);
		deleteData(context);
		handler.sendMessage(Message.obtain());

	}

}
