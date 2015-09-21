/**
 * 
 */
package uk.ac.qub.finalproject.calculationclasses;

import java.io.Serializable;

/**
 * Concrete implementation of the AbstractResultsPacket. This will be sent from
 * the client device to be processed by the server. It stores the ID of the
 * processed work packet and the processed data.
 * 
 * @author Phil
 *
 */
public class ResultsPacket extends AbstractResultsPacket implements
		Serializable {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -5714766945075744825L;

	@Override
	public boolean equals(Object arg) {
		if (arg == null) {
			return false;
		}

		if (!getClass().equals(arg.getClass())) {
			return false;
		}

		IResultsPacket other = (IResultsPacket) arg;
		return getPacketId().equals(other.getPacketId());
	}

	@Override
	public int hashCode() {
		if (getPacketId() != null) {
			return getPacketId().hashCode();
		} else {
			return super.hashCode();
		}
	}

}
