/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * This service will have been triggered either if the server has no more work
 * packets available or if the device has sent too many invalid results. It will
 * check the server for more work packets on a daily basis, at an indeterminate
 * time. This will be useful to avoid all dormant devices sending a request to
 * the server at the same time.
 * 
 * @author Phil
 *
 */
public class DormantService extends Service {

	private static final String INTENT_ID = "uk.ac.qub.finalproject.client.services.alarm";

	private AlarmManager alarmManager;
	private BroadcastReceiver broadcastReceiver;
	private PendingIntent pendingIntent;

	@Override
	public void onCreate() {
		// sets up the alarm manager and the broadcast receiver to listen for
		// the alarm
		alarmManager = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);

		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				checkForNewActivity();
			}
		};

		registerReceiver(broadcastReceiver, new IntentFilter(INTENT_ID));
		
		// sets the alarm to go off every day at an inexact time
		// this will reduce battery usage by only triggering the broadcast
		// receiver when the device is already awake
		pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
				new Intent(INTENT_ID), 0);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY,
				pendingIntent);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
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

	@Override
	public void onDestroy() {
		alarmManager.cancel(pendingIntent);
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	/**
	 * Checks to make sure there is enough storage available to store new work
	 * packets. If the device has enough space available a service to get more
	 * work packets from the server is started.
	 */
	private void checkForNewActivity() {
		long totalSpace = getFilesDir().getTotalSpace();
		long usableSpace = getFilesDir().getUsableSpace();
		int percentSpaceAvailable = (int) (usableSpace / (double) totalSpace * 100);

		if (percentSpaceAvailable > 10) {
			Intent intent = new Intent(this, RequestWorkPacketsService.class);
			startActivity(intent);
		}
	}

}
