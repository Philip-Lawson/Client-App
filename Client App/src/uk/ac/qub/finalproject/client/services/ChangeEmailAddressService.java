/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * This service triggers a networking thread that will retrieve the user's new
 * email address and ask the server to change the email address associated with
 * the device in the database.
 * 
 * @author Phil
 *
 */
public class ChangeEmailAddressService extends IntentService {

	public ChangeEmailAddressService() {
		super(ChangeEmailAddressService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		RunnableClientTemplate changeEmailRunnable = new ChangeEmailRunnable(
				this);
		Thread changeEmailThread = new Thread(changeEmailRunnable);
		changeEmailThread.start();
	}

}
