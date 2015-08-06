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
public class ChangeEmailAddressService extends IntentService {

	public ChangeEmailAddressService() {
		super(ChangeEmailAddressService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		RunnableClientTemplate changeEmailRunnable = new ChangeEmailRunnable(this);
		Thread changeEmailThread = new Thread(changeEmailRunnable);
		changeEmailThread.start();
	}

}
