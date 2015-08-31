/**
 * 
 */
package uk.ac.qub.finalproject.calculationclasses;

import java.io.Serializable;

/**
 * Abstract implementation of IDataProcessor. This class processes the work
 * packet on the client and returns an IResultsPacket. <br>
 * </br>The execute method returns a results packet with the appropriate packet
 * ID and the results of the calculation. It delegates the
 * processing/calculation to the abstract processData method. Any implementation
 * of this class must also implement the Serializable interface and implement
 * the protected processData method.
 * 
 * <br>
 * </br> There is a risk of a ClassCastException in the processData method. The
 * implementor is responsible for handling the exception within the method in an
 * appropriate manner.
 * 
 * @author Phil
 *
 */
public abstract class AbstractDataProcessor implements IDataProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3706390806556723295L;

	@Override
	public final IResultsPacket execute(IWorkPacket workPacket) {
		IResultsPacket resultsPacket = new ResultsPacket();
		Serializable data = processData(workPacket.getInitialData());
		
		resultsPacket.setResult(data);
		resultsPacket.setPacketId(workPacket.getPacketId());

		return resultsPacket;
	}

	/**
	 * To be implemented. This method processes the data from the work packet.
	 * 
	 * @param obj
	 *            the data to be processed. Must be cast to the appropriate
	 *            object within the method.
	 * @return the processed data
	 */
	protected abstract Serializable processData(Serializable obj);

}
