/**
 * 
 */
package finalproject.poc.calculationclasses;

import java.io.Serializable;

/**
 * Concrete implementation of the AbstractResultsPacket. This will be sent from
 * the client device to be processed by the server. It stores the ID of the
 * processed work packet and the processed data.
 * 
 * @author Phil
 *
 */
public class ResultsPacket extends AbstractResultsPacket implements Serializable {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -5714766945075744825L;

}
