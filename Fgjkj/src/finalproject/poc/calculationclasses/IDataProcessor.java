/**
 * 
 */
package finalproject.poc.calculationclasses;

import java.io.Serializable;

/**
 * This represents the data processor that will process the work packet that is
 * sent to the client de.vice
 * 
 * @author Phil
 *
 */
public interface IDataProcessor extends Serializable {

	/**
	 * Executes the calculation necessary to process the data in the work
	 * packet. This method is called in the client device.
	 * 
	 * @param workPacket
	 *            a work packet containing the work packet ID and the data to be
	 *            processed
	 * @return a results packet containing the work packet ID and the processed
	 *         data
	 */
	public IResultsPacket execute(IWorkPacket workPacket);
}
