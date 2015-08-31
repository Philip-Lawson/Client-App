package uk.ac.qub.finalproject.calculationclasses;

import java.io.Serializable;

/**
 * Abstract implementation of IWorkPacket. To be sent from the server to be
 * processed by the client device. This stores the ID of the work packet and the
 * data to be processed.
 * 
 * @author Phil
 *
 */
public abstract class AbstractWorkPacket implements IWorkPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3127345838271149066L;

	private static long DEFAULT_PACKET_ID = 100000000L;

	/**
	 * The ID of the work packet. This ID should be unique.
	 */
	private String packetId;

	/**
	 * The data to be processed.
	 */
	private Serializable initialData;

	/**
	 * Default constructor sets the work packet with a default unique ID.
	 */
	public AbstractWorkPacket() {
		this.setPacketId(++DEFAULT_PACKET_ID + "");
	}

	/**
	 * Instantiates a work packet with a default unique packet ID and the data
	 * to be processed.
	 * 
	 * @param initialData
	 *            the data to be processed
	 */
	public AbstractWorkPacket(Serializable initialData) {
		this();
		this.setInitialData(initialData);
	}

	/**
	 * Instantiates a work packet with a packet ID and the data to be processed.
	 * 
	 * @param packetId
	 *            the ID of the work packet
	 * @param initialData
	 *            the data to be processed
	 */
	public AbstractWorkPacket(String packetId, Serializable initialData) {
		this.setPacketId(packetId);
		this.setInitialData(initialData);
	}

	@Override
	public void setPacketId(String packetId) {
		this.packetId = packetId;
	}

	@Override
	public String getPacketId() {
		return packetId;
	}

	@Override
	public void setInitialData(Serializable initialData) {
		this.initialData = initialData;
	}

	@Override
	public Serializable getInitialData() {
		return initialData;
	}

}
