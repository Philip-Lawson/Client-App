/**
 * 
 */
package finalproject.poc.calculationclasses;

import java.util.ArrayList;

/**
 * @author Phil
 *
 */
public class ResultsPacketList extends ArrayList<IResultsPacket> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 806447377441322468L;
	
	private String deviceID;
	
	
	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

}
