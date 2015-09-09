/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.util.List;

import uk.ac.qub.finalproject.client.persistence.DataStorage;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * The network service checks for any incomplete network tasks and starts all
 * remaining tasks. This service should only be started when a network is
 * available.
 * 
 * @author Phil
 *
 */
public class NetworkService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		DataStorage workDB = FileAndPrefStorage.getInstance(this);
		List<Integer> incompleteTasks = workDB.getIncompleteNetworkActions();
		for (Integer task : incompleteTasks) {
			processTask(task);
		}

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void processTask(int requestNum) {
		switch (requestNum) {
		case (ClientRequest.CHANGE_EMAIL):
			startService(ChangeEmailAddressService.class);
			break;
		case (ClientRequest.PROCESS_RESULT):
			startService(SendResultsService.class);
			break;
		case (ClientRequest.REQUEST_WORK_PACKET):
			startService(RequestWorkPacketsService.class);
			break;
		case (ClientRequest.REQUEST_PROCESSING_CLASS):
			startService(LoadProcessingClassService.class);
			break;
		case (ClientRequest.DEREGISTER_DEVICE):
			break;
		case (ClientRequest.DEREGISTER_USER):
			break;
		}
	}

	private void startService(Class<? extends Service> service) {
		Intent i = new Intent(getApplicationContext(), service);
		getApplicationContext().startService(i);
	}

}
