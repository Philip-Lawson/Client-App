/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.io.IOException;

import uk.ac.qub.finalproject.calculationclasses.RegistrationPack;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import uk.ac.qub.finalproject.s40143289.client.views.R;

/**
 * This runnable sends a request to the server to change the stored email
 * address to the user's new email address. Prior to sending the request it
 * records it in the database and erases the request from the database once
 * completed.
 * 
 * @author Phil
 *
 */
public class ChangeEmailRunnable extends RunnableClientTemplate {

	/**
	 * The registration pack that stores the device's ID and email address. This
	 * will be sent to the server.
	 */
	private RegistrationPack registrationPack;

	/**
	 * A flag to determine if the request to the server was successful.
	 */
	private boolean emailChangedSuccessfully = false;

	public ChangeEmailRunnable(Context context) {
		super(context);
	}

	@Override
	protected void setup() {
		workDB = FileAndPrefStorage.getInstance(context);
		workDB.logNetworkRequest(ClientRequest.CHANGE_EMAIL);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		String deviceID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		String emailAddress = pref.getString(
				context.getString(R.string.email_key), "");
		String defaultAddress = context
				.getString(R.string.home_page_email_default_text);

		boolean userIsAnonymous = pref.getBoolean(
				context.getString(R.string.anonymous_key), false);

		registrationPack = new RegistrationPack();
		registrationPack.setAndroidID(deviceID);

		if (emailAddress.equals(defaultAddress) || userIsAnonymous) {
			// send a blank string rather than allowing a null to be sent
			registrationPack.setEmailAddress("");
		} else {
			registrationPack.setEmailAddress(emailAddress);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.fgjkj.Client#communicateWithServer()
	 */
	@Override
	protected void communicateWithServer() throws IOException {
		output.writeInt(ClientRequest.CHANGE_EMAIL);
		output.writeObject(registrationPack);
		emailChangedSuccessfully = input.readBoolean();
	}

	@Override
	protected void finish() {
		if (emailChangedSuccessfully) {
			workDB.deleteNetworkRequest(ClientRequest.CHANGE_EMAIL);
		}
	}

}
