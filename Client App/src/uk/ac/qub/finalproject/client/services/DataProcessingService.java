/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import uk.ac.qub.finalproject.client.views.R;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

/**
 * This service triggers a thread that processes work packets. The thread is
 * given a lower system priority to ensure that it doesn't consume resources
 * while the user is active on the device. <br>
 * It also shows the user a notification alert that updates them on the current
 * progress of the processing. This is enabled by allowing the processing thread
 * to use a callback to this service.<br>
 * </br> Note that notifications will only be seen if the user has enabled
 * notifications in the app settings.
 * 
 * @author Phil
 *
 */
public class DataProcessingService extends Service {

	/**
	 * The ID of the progress notification. This is used to ensure that only one
	 * notification is pushed to the user.
	 */
	private static int PROGRESS_NOTIFICATION_ID = 1;

	private DataProcessingRunnable dataProcessingRunnable;
	private Thread dataProcessingThread;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder notificationBuilder;

	@Override
	public void onCreate() {
		super.onCreate();
		dataProcessingRunnable = new DataProcessingRunnable(this);
		dataProcessingThread = new Thread(dataProcessingRunnable);
		dataProcessingThread
				.setPriority(Process.THREAD_PRIORITY_LESS_FAVORABLE);

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationBuilder = new NotificationCompat.Builder(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!dataProcessingThread.isAlive()) {
			dataProcessingThread.start();
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (dataProcessingRunnable.isProcessing()) {
			notifyProcessingPaused();
		}

		dataProcessingRunnable.stopRunnable();
		super.onDestroy();
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

	/**
	 * Updates the progress bar to the current status of the calculation.
	 * 
	 * @param packetsComplete
	 * @param packetsLeft
	 */
	public void updateProgress(int packetsComplete, int packetsLeft) {
		if (canShowNotifications()) {
			notificationBuilder
					.setContentTitle(getString(R.string.processing_title));
			notificationBuilder.setProgress(packetsComplete + packetsLeft,
					packetsComplete, false);
			notificationBuilder.setContentText(packetsComplete + " of "
					+ (packetsComplete + packetsLeft) + " Results Complete!");
			notificationManager.notify(PROGRESS_NOTIFICATION_ID,
					notificationBuilder.build());
		}

	}

	/**
	 * Removes the progress bar from the user's notification and replaces the
	 * progress bar with a message telling the user that processing is completed
	 * for now.
	 */
	public void notifyProcessingComplete() {
		if (canShowNotifications()) {
			notificationBuilder
					.setContentTitle(getString(R.string.processing_title));
			notificationBuilder.setProgress(0, 0, false);
			notificationBuilder
					.setContentText(getString(R.string.processing_complete));
			notificationManager.notify(PROGRESS_NOTIFICATION_ID,
					notificationBuilder.build());
		}
	}

	/**
	 * Clears the notification progress bar and replaces it with a message
	 * telling the user that processing has paused. This is used to reassure the
	 * user that their battery constraints are being respected.
	 */
	private void notifyProcessingPaused() {
		if (canShowNotifications()) {
			notificationBuilder
					.setContentTitle(getString(R.string.processing_title));
			notificationBuilder.setProgress(0, 0, false);
			notificationBuilder
					.setContentText(getString(R.string.processing_paused));
			notificationManager.notify(PROGRESS_NOTIFICATION_ID,
					notificationBuilder.build());
		}
	}

	/**
	 * Checks if the user wants to see notifications from this app.
	 * 
	 * @return
	 */
	private boolean canShowNotifications() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		return pref
				.getBoolean(getString(R.string.show_notifications_key), true);
	}

	/**
	 * Start the service that will send the results packet list to the server.
	 */
	public void startResultService() {
		Intent i = new Intent(getApplicationContext(), SendResultsService.class);
		getApplicationContext().startService(i);
		stopSelf();
	}

}
