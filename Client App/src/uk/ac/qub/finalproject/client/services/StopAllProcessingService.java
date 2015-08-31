/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author Phil
 *
 */
public class StopAllProcessingService extends IntentService {

	public StopAllProcessingService(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Class<?>[] services = { BatteryMonitorService.class,
				DataProcessingService.class,
				NetworkService.class, SendResultsService.class };

		for (Class<?> service : services) {
			Intent newIntent = new Intent(this, service);
			stopService(newIntent);
		}
		
		Intent newIntent = new Intent(this, DormantService.class);
		startService(newIntent);

	}

}
