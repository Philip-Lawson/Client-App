/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import uk.ac.qub.finalproject.calculationclasses.ResultsPacketList;
import uk.ac.qub.finalproject.client.persistence.DataStorage;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.app.IntentService;
import android.content.Intent;

/**
 * This service pulls the latest list of results packets from the database. If
 * the list contains results it will start a networking thread that will send
 * the results to the server.
 * 
 * @author Phil
 *
 */
public class SendResultsService extends IntentService {
	
	private DataStorage workDB;

	public SendResultsService() {
		super(SendResultsService.class.getName());		
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		workDB = FileAndPrefStorage.getInstance(getApplicationContext());
		ResultsPacketList resultsList = workDB.loadResultsPacketList();

		if (resultsList.size() <= 0) {
			stopSelf();
		} else {
			RunnableClientTemplate sendResultsRunnable = new SendResultsRunnable(
					this, this, resultsList);
			Thread sendResultsThread = new Thread(sendResultsRunnable);
			sendResultsThread.start();
		}
		
	}

}
