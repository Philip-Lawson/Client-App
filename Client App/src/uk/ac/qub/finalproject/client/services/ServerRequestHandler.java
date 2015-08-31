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
import uk.ac.qub.finalproject.client.views.R;
import uk.ac.qub.finalproject.client.persistence.DataStorage;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;

/**
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

		DataStorage workDB = new FileAndPrefStorage(context);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			DataStorage workDB = new FileAndPrefStorage(context);

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

	private boolean canStartProcessing() {
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		Intent batteryInfo = context.registerReceiver(null, intentFilter);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		return (isCharging(batteryInfo, pref) || aboveThreshold(batteryInfo,
				pref)) && userPermitsProcessing(pref);
	}

	private boolean isCharging(Intent batteryInfo, SharedPreferences pref) {
		boolean chargingEnabled = pref.getBoolean(
				context.getString(R.string.charging_key), true);
		int batteryStatus = batteryInfo.getIntExtra(
				BatteryManager.EXTRA_STATUS, -1);
		boolean charging = ((batteryStatus == BatteryManager.BATTERY_PLUGGED_AC) || (batteryStatus == BatteryManager.BATTERY_PLUGGED_USB));

		return chargingEnabled && charging;

	}

	private boolean userPermitsProcessing(SharedPreferences pref) {
		return pref.getBoolean(
				context.getString(R.string.user_permits_processing_key), true);
	}

	private boolean aboveThreshold(Intent batteryInfo, SharedPreferences pref) {
		String chargingPref = pref.getString(
				context.getString(R.string.battery_limit_key), "0");
		int chargeLimit = Integer.parseInt(chargingPref);

		int level = batteryInfo.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		int scale = batteryInfo.getIntExtra(BatteryManager.EXTRA_SCALE, 0);

		double currentCharge = scale / (double) level;
		double percentageThreshold = chargeLimit / 100.0;

		return currentCharge > percentageThreshold;
	}

}
