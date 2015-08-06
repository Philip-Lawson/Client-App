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
public class LoadProcessingClassService extends IntentService {

	public LoadProcessingClassService(String name) {
		super(LoadProcessingClassService.class.getSimpleName());		
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		RunnableClientTemplate loadProcessingClassRunnable = new LoadProcessingClassRunnable(this);
		Thread loadProcessingClassThread = new Thread(loadProcessingClassRunnable);
		loadProcessingClassThread.start();
	}

}
