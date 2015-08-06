/**
 * 
 */
package finalproject.poc.calculationclasses;

import java.io.Serializable;

/**
 * This represents a results packet that will be sent from the client device and
 * processed in the server. Stores the result of the processed data and the ID
 * of the original work packet.
 * 
 * @author Phil
 *
 */
public interface IResultsPacket extends Serializable {

	/**
	 * Returns the ID of the processed work packet.
	 * 
	 * @return the ID of the processed work packet
	 */
	public String getPacketId();

	/**
	 * Sets the ID of the processed work packet.
	 * 
	 * @param packetId
	 *            the ID of the processed work packet
	 */
	public void setPacketId(String packetId);

	/**
	 * Returns the processed data.
	 * 
	 * @return the processed data
	 */
	public Object getResult();

	/**
	 * Stores the processed data.
	 * 
	 * @param result
	 *            the processed data
	 */
	public void setResult(Object result);
	
}
