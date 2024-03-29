/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;

import uk.ac.qub.finalproject.calculationclasses.IDataProcessor;
import uk.ac.qub.finalproject.calculationclasses.ProcessingClassLoader;
import uk.ac.qub.finalproject.calculationclasses.ResultsPacketList;
import uk.ac.qub.finalproject.calculationclasses.WorkPacketList;
import uk.ac.qub.finalproject.s40143289.client.views.R;
import uk.ac.qub.finalproject.client.persistence.DataStorage;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;

/**
 * This is the concrete implementation of the abstract request handler class. It
 * processes server requests to process work packets, become dormant or to load
 * a new processor class. Note that classloading has not yet been implemented.
 * 
 * @author Phil
 *
 */
public class ServerRequestHandler extends AbstractRequestHandler {

	public ServerRequestHandler(Context context) {
		super(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.services.AbstractRequestHandler#becomeDormant
	 * ()
	 */
	@Override
	protected void becomeDormant() {
		Intent i = new Intent(context, DormantService.class);
		context.startService(i);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.qub.finalproject.client.services.AbstractRequestHandler#
	 * loadProcessingClass(java.io.ObjectInputStream)
	 */
	@Override
	protected void loadProcessingClass(ObjectInputStream input) {

		DataStorage workDB = FileAndPrefStorage.getInstance(context);
		workDB.logNetworkRequest(ClientRequest.REQUEST_PROCESSING_CLASS);

		try {
			byte[] classBytes = (byte[]) input.readObject();

			IDataProcessor processor = ProcessingClassLoader
					.loadClass(classBytes);
			workDB.saveProcessorClass(processor);
			workDB.deleteNetworkRequest(ClientRequest.REQUEST_PROCESSING_CLASS);

			Intent i = new Intent(context, RequestWorkPacketsService.class);
			context.startService(i);
		} catch (OptionalDataException e) {

		} catch (ClassNotFoundException e) {

		} catch (IOException e) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.qub.finalproject.client.services.AbstractRequestHandler#
	 * processWorkPackets(java.io.ObjectInputStream)
	 */
	@Override
	protected void processWorkPackets(ObjectInputStream input) {
		try {
			WorkPacketList workPacketList = (WorkPacketList) input.readObject();
			DataStorage workDB = FileAndPrefStorage.getInstance(context);

			workDB.saveWorkPacketList(workPacketList);
			workDB.saveResultsPacketList(new ResultsPacketList());

			if (canStartProcessing()) {
				Intent i = new Intent(context, DataProcessingService.class);
				context.startService(i);
			}
		} catch (ClassNotFoundException e) {

		} catch (OptionalDataException e) {

		} catch (IOException e) {

		}

	}

	/**
	 * Helper method checks the current battery status to see if it's feasible
	 * to start processing.
	 * 
	 * @return
	 */
	private boolean canStartProcessing() {
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		Intent batteryInfo = context.registerReceiver(null, intentFilter);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		return (isCharging(batteryInfo, pref) || aboveThreshold(batteryInfo,
				pref)) && userPermitsProcessing(pref);
	}

	/**
	 * Checks to see if the battery is charging. Note that if the user does not
	 * have charging enabled this will return false.
	 * 
	 * @param batteryInfo
	 * @param pref
	 * @return
	 */
	private boolean isCharging(Intent batteryInfo, SharedPreferences pref) {
		boolean chargingEnabled = pref.getBoolean(
				context.getString(R.string.charging_key), true);
		int batteryStatus = batteryInfo.getIntExtra(
				BatteryManager.EXTRA_STATUS, -1);
		boolean charging = ((batteryStatus == BatteryManager.BATTERY_PLUGGED_AC) || (batteryStatus == BatteryManager.BATTERY_PLUGGED_USB));

		return chargingEnabled && charging;

	}

	/**
	 * Returns true if the user currently permits processing.
	 * 
	 * @param pref
	 * @return
	 */
	private boolean userPermitsProcessing(SharedPreferences pref) {
		return pref.getBoolean(
				context.getString(R.string.user_permits_processing_key), true);
	}

	/**
	 * Checks to see if the battery charge is above the user's specified
	 * threshold.
	 * 
	 * @param batteryInfo
	 * @param pref
	 * @return
	 */
	private boolean aboveThreshold(Intent batteryInfo, SharedPreferences pref) {
		String chargingPref = pref.getString(
				context.getString(R.string.battery_limit_key), "0");
		
		int chargeLimit;
		
		try {
			chargeLimit = Integer.parseInt(chargingPref);
		} catch(NumberFormatException e){
			return false;
		}

		int level = batteryInfo.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		int scale = batteryInfo.getIntExtra(BatteryManager.EXTRA_SCALE, 0);

		double currentCharge = level / (double) scale;
		double percentageThreshold = chargeLimit / 100.0;

		return currentCharge > percentageThreshold;
	}

}
