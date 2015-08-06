/**
 * 
 */
package finalproject.poc.calculationclasses;

/**
 * Abstract implementation of IResultsPacket. To be sent from the client device
 * to be processed by the server. This stores the ID of the processed work
 * packet and the processed data.
 * 
 * @author Phil
 *
 */
public abstract class AbstractResultsPacket implements IResultsPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = -971802239100813326L;

	/**
	 * The ID of the processed work packet.
	 */
	private String packetId;
		

	/**
	 * The processed data.
	 */
	private Object result;

	@Override
	public void setPacketId(String packetId) {
		this.packetId = packetId;
	}

	@Override
	public String getPacketId() {
		return packetId;
	}

	@Override
	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public Object getResult() {
		return result;
	}
		
}
