/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import uk.ac.qub.finalproject.calculationclasses.ResultsPacketList;
import uk.ac.qub.finalproject.client.persistence.DataStorage;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * This service pulls the latest list of results packets from the database. If
 * the list contains results it will start a networking thread that will send
 * the results to the server.
 * 
 * @author Phil
 *
 */
public class SendResultsService extends Service {

	private DataStorage workDB;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
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

		return START_NOT_STICKY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
