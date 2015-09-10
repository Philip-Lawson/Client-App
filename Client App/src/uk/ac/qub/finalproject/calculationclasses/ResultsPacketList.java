/**
 * 
 */
package uk.ac.qub.finalproject.calculationclasses;

import java.util.ArrayList;

/**
 * A custom collection storing IResultsPackets. Used to send results packets
 * from a client to the server. Each results packet list should contain the
 * timestamp that was stored in its respective work packet list and the ID of
 * the device that processed the work packets.
 * 
 * @author Phil
 *
 */
public class ResultsPacketList extends ArrayList<IResultsPacket> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 806447377441322468L;

	private String deviceID;

	private Long timeStamp;

	public ResultsPacketList() {
		super();
	}

	public ResultsPacketList(String deviceID) {
		super();
		this.deviceID = deviceID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
