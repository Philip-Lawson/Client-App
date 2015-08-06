/**
 * 
 */
package finalproject.poc.calculationclasses;

/**
 * @author Phil
 *
 */
public class CalculationPacket {

	private WorkPacketList workPacketList;
	private ResultsPacketList resultsPacketList;
	
	public CalculationPacket(){
		
	}

	public CalculationPacket(WorkPacketList workPacketList,
			ResultsPacketList resultsPacketList) {
		this.workPacketList = workPacketList;
		this.resultsPacketList = resultsPacketList;
	}

	public WorkPacketList getWorkPacketList() {
		return workPacketList;
	}

	public void setWorkPacketList(WorkPacketList workPacketList) {
		this.workPacketList = workPacketList;
	}

	public ResultsPacketList getResultsPacketList() {
		return resultsPacketList;
	}

	public void setResultsPacketList(ResultsPacketList resultsPacketList) {
		this.resultsPacketList = resultsPacketList;
	}

}
