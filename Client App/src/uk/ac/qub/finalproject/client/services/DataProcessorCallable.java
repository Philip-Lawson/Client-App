/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.util.concurrent.Callable;

import uk.ac.qub.finalproject.calculationclasses.CalculationPacket;
import uk.ac.qub.finalproject.calculationclasses.IDataProcessor;
import uk.ac.qub.finalproject.calculationclasses.ResultsPacketList;
import uk.ac.qub.finalproject.calculationclasses.WorkPacketList;

/**
 * @author Phil
 *
 */
public class DataProcessorCallable implements Callable<CalculationPacket> {
	
	private IDataProcessor dataProcessor;
	private CalculationPacket packet;

	@Override
	public CalculationPacket call() {
		WorkPacketList workList = packet.getWorkPacketList();
		ResultsPacketList resultsList = packet.getResultsPacketList();
		
		resultsList.add(dataProcessor.execute(workList.remove(0)));
		
		packet.setResultsPacketList(resultsList);
		packet.setWorkPacketList(workList);
		return packet ;
	}

	public IDataProcessor getDataProcessor() {
		return dataProcessor;
	}

	public void setDataProcessor(IDataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
	}

	public CalculationPacket getPacket() {
		return packet;
	}

	public void setPacket(CalculationPacket packet) {
		this.packet = packet;
	}

}
