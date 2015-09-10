/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

import java.io.Serializable;

/**
 * A POJO that stores the device ID, an email address and the app's version
 * code. All attributes are accessible using getters and setters.
 * 
 * @author Phil
 *
 */
public class RegistrationPack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5605818805008317998L;

	private String androidID;
	private String emailAddress;
	private int versionCode;

	public String getAndroidID() {
		return androidID;
	}

	public void setAndroidID(String androidID) {
		this.androidID = androidID;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Returns true if the registration pack has an email address.
	 * 
	 * @return
	 */
	public boolean hasEmailAddress() {
		return null != emailAddress;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

}
