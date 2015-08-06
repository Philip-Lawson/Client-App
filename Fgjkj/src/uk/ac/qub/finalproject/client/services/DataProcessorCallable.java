/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.util.concurrent.Callable;

import finalproject.poc.calculationclasses.CalculationPacket;
import finalproject.poc.calculationclasses.IDataProcessor;
import finalproject.poc.calculationclasses.ResultsPacketList;
import finalproject.poc.calculationclasses.WorkPacketList;

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
