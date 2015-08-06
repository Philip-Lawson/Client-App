/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.example.fgjkj.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import uk.ac.qub.finalproject.client.persistence.DataStorage;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import finalproject.poc.calculationclasses.CalculationPacket;
import finalproject.poc.calculationclasses.IDataProcessor;
import finalproject.poc.calculationclasses.ResultsPacketList;
import finalproject.poc.calculationclasses.WorkPacketList;

/**
 * This runnable manages the processing and storage of work and results packets.
 * It is designed to function without having to be aware of the number of work
 * packets that have been processed.<br>
 * </br> To do this, essentially a transfer system has been implemented .The
 * work packet list and the result packet list are taken from the file system. A
 * work packet is removed from the loaded list for processing. When processing
 * is complete the results packet is stored in the loaded list and the changed
 * work packet and results packet lists are stored to file, overwriting the
 * previous work and results packet lists.
 * 
 * @author Phil
 *
 */
public class DataProcessingRunnable implements Runnable {

	/**
	 * Occasionally the reference to the processing service can be killed by the
	 * Android OS. If this happens an update to the user's 'results processed'
	 * icon will be missed. This tracks the number of missed updates.
	 */
	private static int MISSED_UPDATES = 0;

	/**
	 * The DataProcessingService that instantiates the runnable. This is used to
	 * access the application context and to ask the service to notify the user
	 * of progress.
	 */
	private DataProcessingService dataProcessingService;

	/**
	 * The storage system used to read and write data packets.
	 */
	private DataStorage workStorage;

	/**
	 * This determines whether the runnable can continue processing. If the
	 * service must be stopped it will change canContinue to false, causing the
	 * runnable to stop once it's finished processing the current work packet.
	 */
	private boolean canContinue = true;

	/**
	 * A flag to determine whether the runnable is processing work packets.
	 */
	private boolean isProcessing = false;

	/**
	 * Instantiates the runnable with a reference to the data processing service
	 * that called it.
	 * 
	 * @param dataProcessingService
	 */
	public DataProcessingRunnable(DataProcessingService dataProcessingService) {
		this.dataProcessingService = dataProcessingService;
	}

	/**
	 * Stops the runnable once it has finished its current work packet.
	 */
	public void stopRunnable() {
		canContinue = false;
	}

	/**
	 * Determines if the runnable is currently processing data.
	 * 
	 * @return
	 */
	public boolean isProcessing() {
		return isProcessing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		boolean workProcessed = false;

		workStorage = new FileAndPrefStorage(
				dataProcessingService.getApplicationContext());

		WorkPacketList workPackets = workStorage.loadWorkPacketList();
		ResultsPacketList resultPackets;
		IDataProcessor processor = workStorage.loadProcessorClass();

		// stop the service if processing cannot be completed at this point
		if (null == processor || workPackets.size() < 1) {
			dataProcessingService.stopSelf();
		}

		// set up the thread pool, callable and calculation objects
		isProcessing = true;
		ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		DataProcessorCallable dataProcessor = new DataProcessorCallable();
		dataProcessor.setDataProcessor(processor);
		Callable<CalculationPacket> processorThread;
		CalculationPacket calculationPacket = new CalculationPacket();

		while (workPackets.size() > 0 && canContinue) {
			// set work and result packets
			workPackets = workStorage.loadWorkPacketList();
			resultPackets = workStorage.loadResultsPacketList();
			calculationPacket.setWorkPacketList(workPackets);
			calculationPacket.setResultsPacketList(resultPackets);

			// notify the user of current progress
			// if the process has been stopped we don't want to show a
			// progress bar to the user.
			if (canContinue) {
				dataProcessingService.updateProgress(resultPackets.size(),
						workPackets.size());
			}

			// prepare the callable
			dataProcessor.setPacket(calculationPacket);
			processorThread = dataProcessor;
			Future<CalculationPacket> future = executor.submit(processorThread);

			// start processing and save the result when done
			try {
				calculationPacket = future.get();
				resultPackets = calculationPacket.getResultsPacketList();
				workPackets = calculationPacket.getWorkPacketList();

				workStorage.saveResultsPacketList(resultPackets);
				workStorage.saveWorkPacketList(workPackets);

				workProcessed = true;
				updatePacketsProcessed();

			} catch (InterruptedException e) {
				// TODO log exception?
			} catch (ExecutionException e) {
				// TODO log exception?
			}

		}

		// if processing is complete the service will
		if (workProcessed && workPackets.size() == 0) {
			isProcessing = false;

			try {
				dataProcessingService.notifyProcessingComplete();
				dataProcessingService.startResultService();
			} catch (NullPointerException NPEx) {
				workStorage.logNetworkRequest(ClientRequest.PROCESS_RESULT);
			}

		}

	}

	/**
	 * This helper method updates the number of packets processed in the users
	 * shared preferences. Whenever the shared preference updates it will update
	 * the number on the appropriate page displaying this number.
	 * 
	 * If the service has been killed it will increment the number of missed
	 * updates and apply them at the next update.
	 */
	private void updatePacketsProcessed() {
		try {
			// this call can cause a null pointer if the service has been
			// stopped and
			// the runnable is still processing
			Context context = dataProcessingService.getApplicationContext();

			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);
			int packetsProcessed = pref.getInt(
					context.getString(R.string.packets_completed_key), 0);
			pref.edit()
					.putInt(context.getString(R.string.packets_completed_key),
							++packetsProcessed + MISSED_UPDATES).apply();
			MISSED_UPDATES = 0;
		} catch (NullPointerException NPEx) {
			++MISSED_UPDATES;
		}
	}

}
