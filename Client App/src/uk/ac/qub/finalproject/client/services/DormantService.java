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
		alarmManager = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);

		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				checkForNewActivity();
			}
		};

		registerReceiver(broadcastReceiver, new IntentFilter(INTENT_ID));

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
