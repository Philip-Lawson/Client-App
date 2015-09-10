/**
 * 
 */
package uk.ac.qub.finalproject.client.persistence;

import java.util.List;

import uk.ac.qub.finalproject.calculationclasses.IDataProcessor;
import uk.ac.qub.finalproject.calculationclasses.ResultsPacketList;
import uk.ac.qub.finalproject.calculationclasses.WorkPacketList;

/**
 * @author Phil
 *
 */
public interface DataStorage {

	/**
	 * Sets up the storage system. This should be called when the app is first
	 * loaded.
	 */
	public void setupStorage();

	/**
	 * Deletes all data - user information, work packet lists and network
	 * requests.
	 */
	public void deleteAllData();

	/**
	 * Saves a results packet list to the persistence system.
	 * 
	 * @param resultsList
	 */
	public void saveResultsPacketList(ResultsPacketList resultsList);

	/**
	 * Loads the results packet list stored in the persistence system.
	 * 
	 * @return
	 */
	public ResultsPacketList loadResultsPacketList();

	/**
	 * Saves a work packet list to the persistence system.
	 * 
	 * @param workPacketList
	 */
	public void saveWorkPacketList(WorkPacketList workPacketList);

	/**
	 * Loads the work packet list stored in the persistence system.
	 * 
	 * @return
	 */
	public WorkPacketList loadWorkPacketList();

	/**
	 * Saves the processor class to the persistence system.
	 * 
	 * @param dataProcessor
	 */
	public void saveProcessorClass(IDataProcessor dataProcessor);

	/**
	 * Loads the data processor instance from the persistence system.
	 * 
	 * @return
	 */
	public IDataProcessor loadProcessorClass();

	/**
	 * Writes a network request to the persistence system. This request will be
	 * acted upon when wifi is next available.
	 * 
	 * @param requestNum
	 */
	public void logNetworkRequest(int requestNum);

	/**
	 * Returns a list of all incomplete network requests.
	 * 
	 * @return
	 */
	public List<Integer> getIncompleteNetworkActions();

	/**
	 * Deletes a network request from the persistence system. This should be
	 * called when the network request is completed.
	 * 
	 * @param requestNum
	 */
	public void deleteNetworkRequest(int requestNum);

	/**
	 * Asks the persistence system to transfer data from internal storage to
	 * external storage or vice versa, depending on the user's preferences.
	 */
	public void transferFiles();

}
