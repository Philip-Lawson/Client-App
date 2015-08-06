/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

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
 * @author Phil
 *
 */
public class DeleteAccountRunnable implements Runnable {

	private Context context;
	private Handler handler;
	
	public DeleteAccountRunnable(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
	}
	
	private void deletePreferences(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		pref.edit().clear().commit();		
	}

	private void unregisterReceivers(Context context) {
		ComponentName networkInfoListener = new ComponentName(context,
				NetworkInfoReceiver.class);
		context.getPackageManager().setComponentEnabledSetting(
				networkInfoListener,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}

	private void stopServices(Context context) {
		Class<?>[] services = { BatteryMonitorService.class,
				DataProcessingService.class, DormantService.class,
				NetworkService.class, SendResultsService.class };

		for (Class<?> service : services) {
			Intent intent = new Intent(context, service);
			context.stopService(intent);
		}
	}

	private void deleteData(Context context) {
		DataStorage persistence = new FileAndPrefStorage(context);
		persistence.deleteAllData();
	}
	
	@Override
	public void run() {
		deletePreferences(context);
		unregisterReceivers(context);
		stopServices(context);
		deleteData(context);
		handler.handleMessage(Message.obtain());

	}

}
