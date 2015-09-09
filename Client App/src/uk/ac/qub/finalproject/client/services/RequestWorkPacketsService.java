/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * This service starts a networking thread that will request work packets from
 * the server.
 * 
 * @author Phil
 *
 */
public class RequestWorkPacketsService extends IntentService {

	public RequestWorkPacketsService() {
		super(RequestWorkPacketsService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		RunnableClientTemplate requestWorkPacketRunnable = new RequestWorkPacketRunnable(
				this, this);
		Thread requestWorkPacketThread = new Thread(requestWorkPacketRunnable);
		requestWorkPacketThread.start();

	}

}
