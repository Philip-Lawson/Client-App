/**
 * 
 */
package uk.ac.qub.finalproject.calculationclasses;

import java.util.ArrayList;

/**
 * A custom collection of IWorkPackets. Used to send work packets from the
 * server to the client. Each work packet list is timestamped when it is sent to
 * a client device.
 * 
 * @author Phil
 *
 */
public class WorkPacketList extends ArrayList<IWorkPacket> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9113939581476091482L;

	private long timeStamp;

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
