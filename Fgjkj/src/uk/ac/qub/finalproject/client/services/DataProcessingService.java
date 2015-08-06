/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import com.example.fgjkj.R;

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
 * @author Phil
 *
 */
public class DataProcessingService extends Service {

	private static int PROGRESS_BAR_ID = 1;

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
		if (dataProcessingRunnable.isProcessing())
			notifyProcessingPaused();

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
		// TODO Auto-generated method stub
		return null;
	}

	public void updateProgress(int packetsComplete, int packetsLeft) {
		if (canShowNotifications()) {
			notificationBuilder
					.setContentTitle(getString(R.string.processing_title));
			notificationBuilder.setProgress(packetsComplete + packetsLeft,
					packetsComplete, false);
			notificationBuilder.setContentText(packetsComplete + " of "
					+ (packetsComplete + packetsLeft) + " Results Complete!");
			notificationManager.notify(PROGRESS_BAR_ID,
					notificationBuilder.build());
		}

	}

	public void notifyProcessingComplete() {
		if (canShowNotifications()) {
			notificationBuilder
					.setContentTitle(getString(R.string.processing_title));
			notificationBuilder.setProgress(0, 0, false);
			notificationBuilder
					.setContentText(getString(R.string.processing_complete));
			notificationManager.notify(PROGRESS_BAR_ID,
					notificationBuilder.build());
		}
	}

	private void notifyProcessingPaused() {
		if (canShowNotifications()) {
			notificationBuilder
					.setContentTitle(getString(R.string.processing_title));
			notificationBuilder.setProgress(0, 0, false);
			notificationBuilder
					.setContentText(getString(R.string.processing_paused));
			notificationManager.notify(PROGRESS_BAR_ID,
					notificationBuilder.build());
		}
	}

	private boolean canShowNotifications() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		return pref
				.getBoolean(getString(R.string.show_notifications_key), true);
	}

	public void startResultService() {
		Intent i = new Intent(getApplicationContext(), SendResultsService.class);
		getApplicationContext().startService(i);
		stopSelf();
	}

}
