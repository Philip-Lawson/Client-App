/**
 * 
 */
package uk.ac.qub.finalproject.client.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import uk.ac.qub.finalproject.calculationclasses.IDataProcessor;
import uk.ac.qub.finalproject.calculationclasses.ResultsPacketList;
import uk.ac.qub.finalproject.calculationclasses.WorkPacketList;
import uk.ac.qub.finalproject.client.implementations.Implementations;
import uk.ac.qub.finalproject.client.services.StopAllProcessingService;
import uk.ac.qub.finalproject.s40143289.client.views.R;

/**
 * An implementation of the Data Storage interface. This stores the results list
 * and work packet list in the underlying filing system of the device. It takes
 * the user's preference for internal/external storage into account, but
 * defaults to internal storage if external storage is unavailable.
 * 
 * This implementation takes possible SD card removal into account while
 * retrieving data. If internal storage becomes higher than 90% all processing
 * action are stopped, and the dormant service is initiated. Processing will
 * resume once enough storage space becomes available.
 * 
 * @author Phil
 *
 */
public class FileAndPrefStorage implements DataStorage {

	private static final String RESULTS_LIST_FILENAME = "results_list.ser";
	private static final String WORK_LIST_FILENAME = "work_list.ser";
	private static final String PROCESSOR_CLASS_FILENAME = "processor_class.ser";
	private static final String DIRECTORY_NAME = "Volunteer Science Data";

	private static volatile FileAndPrefStorage uniqueInstance;

	private Context context;
	private ObjectOutputStream out;
	private FileOutputStream fileOut;
	private ObjectInputStream in;
	private FileInputStream fileIn;

	private FileAndPrefStorage(Context context) {
		this.context = context.getApplicationContext();
	}

	/**
	 * Returns an instance of the FileAndPrefStorage class. This implements a
	 * double checked locking Singleton, and all public methods are thread-safe.
	 * 
	 * @param context
	 *            the application context
	 * @return
	 */
	public static FileAndPrefStorage getInstance(Context context) {
		if (uniqueInstance == null) {
			synchronized (FileAndPrefStorage.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new FileAndPrefStorage(context);
				}
			}
		}

		return uniqueInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.qub.finalproject.client.persistence.DataStorage#setupStorage()
	 */
	@Override
	public void setupStorage() {
		saveWorkPacketList(new WorkPacketList());
		saveResultsPacketList(new ResultsPacketList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.DataStorage#deleteAllData()
	 */
	@Override
	public synchronized void deleteAllData() {
		String[] files = { WORK_LIST_FILENAME, RESULTS_LIST_FILENAME,
				PROCESSOR_CLASS_FILENAME };

		for (String file : files) {
			deleteFileFromInternal(file);
			deleteFileFromExternal(file);
		}

		if (isExternalStorageWritable()) {
			File dir = new File(
					context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
					DIRECTORY_NAME);
			dir.delete();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.DataStorage#saveResultsPacketList
	 * (uk.ac.qub.finalproject.calculationclasses.ResultsPacketList)
	 */
	@Override
	public synchronized void saveResultsPacketList(ResultsPacketList resultsList) {
		saveFile(RESULTS_LIST_FILENAME, resultsList);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.DataStorage#loadResultsPacketList
	 * ()
	 */
	@Override
	public synchronized ResultsPacketList loadResultsPacketList() {
		ResultsPacketList internalList = (ResultsPacketList) blankList(RESULTS_LIST_FILENAME);
		ResultsPacketList externalList = (ResultsPacketList) blankList(RESULTS_LIST_FILENAME);

		try {
			internalList = (ResultsPacketList) readFileFromInternal(RESULTS_LIST_FILENAME);
		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		}

		try {
			if (isExternalStorageReadable()) {
				externalList = (ResultsPacketList) readFileFromExternal(RESULTS_LIST_FILENAME);
			}
		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		}

		internalList.addAll(externalList);

		if (internalList.getTimeStamp() == 0
				&& externalList.getTimeStamp() == 0) {
			// if neither of the lists have time stamps
			// that means they are both empty,
			// the getTimeStamp method will not
			// be called on an empty list

		} else if (externalList.getTimeStamp() == 0) {
			// the internal list will have a time stamp
		} else if (internalList.getTimeStamp() == 0) {
			internalList.setTimeStamp(externalList.getTimeStamp());
		} else if (externalList.getTimeStamp() < internalList.getTimeStamp()) {
			// set the timestamp to the earlier timestamp
			// this will give a more accurate reflection of
			// processing time needed
			internalList.setTimeStamp(externalList.getTimeStamp());
		}

		return internalList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.DataStorage#saveWorkPacketList
	 * (uk.ac.qub.finalproject.calculationclasses.WorkPacketList)
	 */
	@Override
	public synchronized void saveWorkPacketList(WorkPacketList workPacketList) {
		saveFile(WORK_LIST_FILENAME, workPacketList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.DataStorage#loadWorkPacketList
	 * ()
	 */
	@Override
	public synchronized WorkPacketList loadWorkPacketList() {
		WorkPacketList internalList = (WorkPacketList) blankList(WORK_LIST_FILENAME);
		WorkPacketList externalList = (WorkPacketList) blankList(WORK_LIST_FILENAME);

		try {
			internalList = (WorkPacketList) readFileFromInternal(WORK_LIST_FILENAME);
		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		}

		try {
			if (isExternalStorageReadable()) {
				externalList = (WorkPacketList) readFileFromExternal(WORK_LIST_FILENAME);
			}
		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		}

		internalList.addAll(externalList);

		if (internalList.getTimeStamp() == 0
				&& externalList.getTimeStamp() == 0) {
			// if neither of the lists have time stamps
			// that means they are both empty,
			// the getTimeStamp method will not
			// be called on an empty list

		} else if (externalList.getTimeStamp() == 0) {
			// the internal list will have a time stamp
		} else if (internalList.getTimeStamp() == 0) {
			internalList.setTimeStamp(externalList.getTimeStamp());
		} else if (externalList.getTimeStamp() < internalList.getTimeStamp()) {
			// set the timestamp to the earlier timestamp
			// this will give a more accurate reflection of
			// processing time needed
			internalList.setTimeStamp(externalList.getTimeStamp());
		}

		return internalList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.DataStorage#saveProcessorClass
	 * (uk.ac.qub.finalproject.calculationclasses.IDataProcessor)
	 */
	@Override
	public synchronized void saveProcessorClass(IDataProcessor dataProcessor) {
		// Empty implementation. This will be used if dynamic
		// class loading is implemented in the future.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.DataStorage#loadProcessorClass
	 * ()
	 */
	@Override
	public synchronized IDataProcessor loadProcessorClass() {
		// Working on the assumption that dynamic class loading may be
		// implemented in the future. Returning the processor from here means
		// that the changes needed for loading are kept to this method. The data
		// processing service classes will not need to be changed.
		return Implementations.getDataProcessor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#logNetworkRequest
	 * (int)
	 */
	@Override
	public synchronized void logNetworkRequest(int requestNum) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String requests = pref.getString(
				context.getString(R.string.tasks_list_key), "");
		String newRequest = requestNum + "";

		if (!requests.contains(newRequest)) {
			pref.edit()
					.putString(context.getString(R.string.tasks_list_key),
							requests + requestNum).apply();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.qub.finalproject.client.persistence.WorkDatabase#
	 * getIncompleteNetworkActions()
	 */
	@Override
	public synchronized List<Integer> getIncompleteNetworkActions() {
		ArrayList<Integer> requestsList = new ArrayList<Integer>();

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String requests = pref.getString(
				context.getString(R.string.tasks_list_key), "");

		for (int count = 0; count < requests.length(); count++) {
			try {
				String num = requests.charAt(count) + "";
				requestsList.add(Integer.parseInt(num));
			} catch (NumberFormatException e) {
				// don't add to list if it's not a number!
			}
		}

		return requestsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#deleteNetworkRequest
	 * (int)
	 */
	@Override
	public synchronized void deleteNetworkRequest(int requestNum) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String requests = pref.getString(
				context.getString(R.string.tasks_list_key), "");
		String numToDelete = requestNum + "";

		if (requests.contains(numToDelete)) {
			String amendedRequests = requests.replace(numToDelete, "");
			pref.edit()
					.putString(context.getString(R.string.tasks_list_key),
							amendedRequests).apply();
		}

	}

	@Override
	public synchronized void transferFiles() {
		WorkPacketList workPackets = loadWorkPacketList();
		ResultsPacketList resultsPackets = loadResultsPacketList();
		saveWorkPacketList(workPackets);
		saveResultsPacketList(resultsPackets);
	}

	/**
	 * Reads an object from internal storage based on the filename given.
	 * 
	 * @param fileName
	 * @return
	 * @throws OptionalDataException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private Serializable readFileFromInternal(String fileName)
			throws OptionalDataException, ClassNotFoundException, IOException {
		try {
			fileIn = context.openFileInput(fileName);
			in = new ObjectInputStream(fileIn);

			// the finally block will complete before the return command
			// to avoid reading from a potentially closed stream the object
			// reference must be passed to a placeholder object
			Serializable ser = (Serializable) in.readObject();
			return ser;
		} finally {
			try {
				if (null != in)
					in.close();
			} catch (IOException IOEx) {

			}

			try {
				if (null != fileIn)
					fileIn.close();
			} catch (IOException IOEx) {

			}
		}
	}

	/**
	 * Reads an object from external memory, generally speaking this will be
	 * from the SD card.
	 * 
	 * @param fileName
	 * @return
	 * @throws StreamCorruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Serializable readFileFromExternal(String fileName)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		File dir = new File(
				context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
				DIRECTORY_NAME);
		File file = new File(dir, fileName);

		try {

			if (file.exists()) {
				fileIn = new FileInputStream(file);
				in = new ObjectInputStream(fileIn);
				Serializable ser = (Serializable) in.readObject();
				return ser;
			}

			// If the file doesn't exist we want to return a blank
			// list - not a null object.
			return blankList(fileName);

		} finally {
			// We only want to throw an exception if there's an issue reading
			// the file. The calling method doesn't need to know about an
			// exception closing the input streams.
			try {
				if (null != in)
					in.close();
			} catch (IOException IOEx) {

			}

			try {
				if (null != fileIn)
					fileIn.close();
			} catch (IOException IOEx) {

			}

		}

	}

	/**
	 * Saves the object to file based on the user's storage preferences.
	 * 
	 * @param fileName
	 * @param ser
	 */
	private void saveFile(String fileName, Serializable ser) {
		if (writeToSDCardPreference()) {
			saveFileToExternal(fileName, ser);
		} else {
			saveFileToInternal(fileName, ser);
		}

	}

	/**
	 * Saves a file to internal memory. If the file is written successfully to
	 * internal memory it will delete the file from external memory.
	 * 
	 * @param fileName
	 * @param ser
	 */
	private void saveFileToInternal(String fileName, Serializable ser) {
		long totalSpace = context.getFilesDir().getTotalSpace();
		long usableSpace = context.getFilesDir().getUsableSpace();
		int percentSpaceAvailable = (int) (usableSpace / (double) totalSpace * 100);

		if (percentSpaceAvailable > 10) {
			try {

				fileOut = context
						.openFileOutput(fileName, Context.MODE_PRIVATE);
				out = new ObjectOutputStream(fileOut);
				out.writeObject(ser);
			} catch (IOException e) {

			}

			deleteFileFromExternal(fileName);
		} else {
			try {
				cleanInternalMemory();
			} catch (IOException e) {

			}

			stopAllProcessing();
		}

	}

	/**
	 * Saves the file to external memory. If it is not possible to save the file
	 * to external memory it will default to internal memory. If the file is
	 * successfully saved to external memory a blank version of the object will
	 * be stored in internal memory.
	 * 
	 * @param fileName
	 * @param ser
	 */
	private void saveFileToExternal(String fileName, Serializable ser) {
		File dir = new File(
				context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
				DIRECTORY_NAME);

		if (isExternalStorageWritable() && (dir.mkdirs() || dir.isDirectory())) {
			// write to external
			File file = new File(dir, fileName);
			try {
				fileOut = new FileOutputStream(file);
				out = new ObjectOutputStream(fileOut);
				out.writeObject(ser);

				// save blank file to internal
				saveFileToInternal(fileName, blankList(fileName));

			} catch (IOException ioEx) {
				// if there's a problem default to internal storage
				saveFileToInternal(fileName, ser);
			}

		} else {
			saveFileToInternal(fileName, ser);
		}
	}

	/**
	 * Deletes the file from internal memory. No side effects.
	 * 
	 * @param fileName
	 */
	private void deleteFileFromInternal(String fileName) {
		context.deleteFile(fileName);
	}

	/**
	 * Deletes the file from external memory. No side effects.
	 * 
	 * @param fileName
	 */
	private void deleteFileFromExternal(String fileName) {
		if (isExternalStorageWritable()) {
			File dir = new File(
					context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
					DIRECTORY_NAME);
			File file = new File(dir, fileName);
			file.delete();
		}
	}

	/**
	 * Returns true if the user preference is to write to the SD card.
	 * 
	 * @return
	 */
	private boolean writeToSDCardPreference() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String storagePref = pref.getString(
				context.getString(R.string.storage_key),
				context.getString(R.string.storage_option_internal));

		return storagePref.equals(context
				.getString(R.string.storage_option_sd_card));
	}

	/**
	 * Returns true if it is possible to write to external storage.
	 * 
	 * @return
	 */
	private boolean isExternalStorageWritable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * Returns true if it is possible to read files from external storage.
	 * 
	 * @return
	 */
	private boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}

	/**
	 * This helper method returns a blank work packet list or results packet
	 * list. Returns a results packet list if the results list filename is
	 * passed in. Defaults to a work packet list.
	 * 
	 * @param fileName
	 * @return
	 */
	private Serializable blankList(String fileName) {
		// cannot use a switch here due to backwards compatibility issues
		// needs to work on devices using java 1.7 and below
		if (fileName.equals(RESULTS_LIST_FILENAME)) {
			return new ResultsPacketList();
		} else {
			return new WorkPacketList();
		}
	}

	/**
	 * Helper method replaces the work and result list with blank lists. This
	 * method is called when there is not sufficient space to store any more
	 * data.
	 * 
	 * @throws IOException
	 */
	private void cleanInternalMemory() throws IOException {
		deleteFileFromInternal(WORK_LIST_FILENAME);
		deleteFileFromInternal(RESULTS_LIST_FILENAME);
		fileOut = context.openFileOutput(WORK_LIST_FILENAME,
				Context.MODE_PRIVATE);
		out = new ObjectOutputStream(fileOut);
		out.writeObject(new WorkPacketList());

		fileOut = context.openFileOutput(RESULTS_LIST_FILENAME,
				Context.MODE_PRIVATE);
		out = new ObjectOutputStream(fileOut);
		out.writeObject(new ResultsPacketList());
	}

	/**
	 * Helper method, starts a service that will pause all processing. This
	 * should be called when there isn't enough space to store packet lists.
	 */
	private void stopAllProcessing() {
		Intent intent = new Intent(context, StopAllProcessingService.class);
		context.startService(intent);
	}

}
