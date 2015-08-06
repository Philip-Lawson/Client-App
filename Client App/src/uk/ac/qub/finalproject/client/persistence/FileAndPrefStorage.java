/**
 * 
 */
package uk.ac.qub.finalproject.client.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.finalproject.client.views.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import finalproject.poc.calculationclasses.IDataProcessor;
import finalproject.poc.calculationclasses.ResultsPacketList;
import finalproject.poc.calculationclasses.WorkPacketList;

/**
 * @author Phil
 *
 */
public class FileAndPrefStorage implements DataStorage {

	private static final String RESULTS_LIST_FILENAME = "results_list.ser";
	private static final String WORK_LIST_FILENAME = "work_list.ser";
	private static final String PROCESSOR_CLASS_FILENAME = "processor_class.ser";
	private static final String DIRECTORY_NAME = "Volunteer Science Data";

	private Context context;
	private ObjectOutputStream out;
	private FileOutputStream fileOut;
	private ObjectInputStream in;
	private FileInputStream fileIn;

	public FileAndPrefStorage(Context context) {
		this.context = context;
	}

	@Override
	public void setupStorage() {
		saveResultsPacketList(new ResultsPacketList());
		saveWorkPacketList(new WorkPacketList());
		saveProcessorClass(null);
	}

	@Override
	public void deleteAllData() {
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
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#openDatabase()
	 */
	@Override
	public void openDatabase() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#closeDatabase()
	 */
	@Override
	public void closeDatabase() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#saveResultsPacketList
	 * (finalproject.poc.calculationclasses.ResultsPacketList)
	 */
	@Override
	public void saveResultsPacketList(ResultsPacketList resultsList) {
		saveFile(RESULTS_LIST_FILENAME, resultsList);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#loadResultsPacketList
	 * ()
	 */
	@Override
	public ResultsPacketList loadResultsPacketList() {
		ResultsPacketList resultsPacketList = (ResultsPacketList) readFile(RESULTS_LIST_FILENAME);

		if (null == resultsPacketList) {
			resultsPacketList = new ResultsPacketList();
		}

		return resultsPacketList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#saveWorkPacketList
	 * (finalproject.poc.calculationclasses.WorkPacketList)
	 */
	@Override
	public void saveWorkPacketList(WorkPacketList workPacketList) {
		saveFile(WORK_LIST_FILENAME, workPacketList);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#loadWorkPacketList
	 * ()
	 */
	@Override
	public WorkPacketList loadWorkPacketList() {
		WorkPacketList workPacketsList = (WorkPacketList) readFile(WORK_LIST_FILENAME);

		if (null == workPacketsList) {
			workPacketsList = new WorkPacketList();
		}

		return workPacketsList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#saveProcessorClass
	 * (finalproject.poc.calculationclasses.IDataProcessor)
	 */
	@Override
	public void saveProcessorClass(IDataProcessor dataProcessor) {
		saveFileToInternal(PROCESSOR_CLASS_FILENAME, dataProcessor);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#loadProcessorClass
	 * ()
	 */
	@Override
	public IDataProcessor loadProcessorClass() {
		try {
			return (IDataProcessor) readFileFromInternal(PROCESSOR_CLASS_FILENAME);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.finalproject.client.persistence.WorkDatabase#logNetworkRequest
	 * (int)
	 */
	@Override
	public void logNetworkRequest(int requestNum) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String requests = pref.getString(
				context.getString(R.string.tasks_list_key), "");
		String newRequest = Integer.valueOf(requestNum).toString();

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
	public List<Integer> getIncompleteNetworkActions() {
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
				// don't add to list
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
	public void deleteNetworkRequest(int requestNum) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String requests = pref.getString(
				context.getString(R.string.tasks_list_key), "");
		String numToDelete = Integer.valueOf(requestNum).toString();

		if (requests.contains(numToDelete)) {
			String amendedRequests = requests.replace(numToDelete, "");
			pref.edit()
					.putString(context.getString(R.string.tasks_list_key),
							amendedRequests).apply();
		}

	}

	public boolean transferFiles() {
		// TODO - needs to be tightened to deal with storage issues
		WorkPacketList workList = (WorkPacketList) readFile(WORK_LIST_FILENAME);
		ResultsPacketList resultList = (ResultsPacketList) readFile(RESULTS_LIST_FILENAME);

		if (null != workList && null != resultList) {
			saveFile(WORK_LIST_FILENAME, workList);
			saveFile(RESULTS_LIST_FILENAME, resultList);
			return true;
		} else {
			return false;
		}

	}

	private void saveFile(String fileName, Serializable ser) {
		if (writeExternal()) {
			saveFileToExternal(fileName, ser);
			deleteFileFromInternal(fileName);
		} else {
			saveFileToInternal(fileName, ser);
			deleteFileFromExternal(fileName);
		}

	}

	private Object readFile(String fileName) {
		try {
			return readFileFromInternal(fileName);
		} catch (FileNotFoundException Ex) {
			if (isExternalStorageReadable()) {
				return readFileFromExternal(fileName);
			}
		}

		return null;
	}

	private Object readFileFromInternal(String fileName)
			throws FileNotFoundException {

		try {
			fileIn = context.openFileInput(fileName);
			in = new ObjectInputStream(fileIn);
			return in.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != in)
					in.close();
			} catch (IOException Ex) {

			}
			try {
				if (null != fileIn)
					fileIn.close();
			} catch (IOException Ex) {

			}
		}
		return null;
	}

	private Object readFileFromExternal(String fileName) {

		try {
			File dir = new File(
					context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
					DIRECTORY_NAME);
			File file = new File(dir, fileName);

			if (file.exists()) {
				fileIn = new FileInputStream(file);
				in = new ObjectInputStream(fileIn);
				return in.readObject();
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != in)
					in.close();
			} catch (IOException Ex) {

			}
			try {
				if (null != fileIn)
					fileIn.close();
			} catch (IOException Ex) {

			}
		}
		return null;
	}

	private void saveFileToInternal(String fileName, Serializable ser) {
		try {
			fileOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(ser);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != out)
					out.close();
			} catch (IOException Ex) {

			}
			try {
				if (null != fileOut)
					fileOut.close();
			} catch (IOException Ex) {

			}
		}
	}

	private void saveFileToExternal(String fileName, Serializable ser) {
		File dir = new File(
				context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
				DIRECTORY_NAME);

		if (dir.mkdirs() || dir.isDirectory()) {

			try {
				File file = new File(dir, fileName);
				fileOut = new FileOutputStream(file);
				out = new ObjectOutputStream(fileOut);
				out.writeObject(ser);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != out)
						out.close();
				} catch (IOException Ex) {

				}
				try {
					if (null != fileOut)
						fileOut.close();
				} catch (IOException Ex) {

				}
			}
		}

	}

	private void deleteFileFromInternal(String fileName) {
		context.deleteFile(fileName);
	}

	private void deleteFileFromExternal(String fileName) {
		if (isExternalStorageWritable()) {
			File dir = new File(
					context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
					DIRECTORY_NAME);
			File file = new File(dir, fileName);
			file.delete();
		}
	}

	private boolean writeExternal() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String storagePref = pref.getString(
				context.getString(R.string.storage_key),
				context.getString(R.string.storage_option_internal));

		return storagePref.equals(context
				.getString(R.string.storage_option_sd_card))
				&& isExternalStorageWritable();
	}

	private boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	private boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

}
