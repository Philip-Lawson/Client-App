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
	
	public void setupStorage();
	public void deleteAllData();
	
	public void saveResultsPacketList(ResultsPacketList resultsList);
	public ResultsPacketList loadResultsPacketList();
	
	public void saveWorkPacketList(WorkPacketList workPacketList);
	public WorkPacketList loadWorkPacketList();
	
	public void saveProcessorClass(IDataProcessor dataProcessor);
	public IDataProcessor loadProcessorClass();
	
	public void logNetworkRequest(int requestNum);
	public List<Integer> getIncompleteNetworkActions();
	public void deleteNetworkRequest(int requestNum);
	
	public void transferFiles();
	
	
}
