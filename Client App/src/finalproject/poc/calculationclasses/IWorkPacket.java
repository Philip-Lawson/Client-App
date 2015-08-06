package finalproject.poc.calculationclasses;

import java.io.Serializable;

/**
 * This represents a work packet that will be sent from the server to a client
 * device. A work packet must be able to store and retrieve a packet ID and the
 * data to be processed by the client.
 * 
 * @author Phil
 *
 */
public interface IWorkPacket extends Serializable {

	/**
	 * Returns the packet ID.
	 * 
	 * @return the ID of the work packet
	 */
	public String getPacketId();

	/**
	 * Sets the packet ID
	 * 
	 * @param packetId
	 *            the ID of the work packet
	 */
	public void setPacketId(String packetId);

	/**
	 * Returns the data to be processed.
	 * 
	 * @return the data to be processed
	 */
	public Object getInitialData();

	/**
	 * Sets the data to be processed.
	 * 
	 * @param initialData
	 *            the data to be processed
	 */
	public void setInitialData(Object initialData);
}
