/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.io.IOException;

import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import uk.ac.qub.finalproject.client.views.R;
import uk.ac.qub.finalproject.client.views.RegistrationPack;

/**
 * @author Phil
 *
 */
public class ChangeEmailRunnable extends RunnableClientTemplate {

	private RegistrationPack registrationPack;
	private boolean success = false;

	public ChangeEmailRunnable(Context context) {
		super(context);
	}

	@Override
	protected void setup() {
		workDB = new FileAndPrefStorage(context);
		workDB.logNetworkRequest(ClientRequest.CHANGE_EMAIL);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		String deviceID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		String emailAddress = pref.getString(
				context.getString(R.string.email_key), "");
		String defaultAddress = context
				.getString(R.string.my_account_email_default_text);

		registrationPack = new RegistrationPack();
		registrationPack.setAndroidID(deviceID);

		if (!emailAddress.equals(defaultAddress)) {
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
		success = input.readBoolean();
	}

	@Override
	protected void finish() {
		if (success) {
			workDB.deleteNetworkRequest(ClientRequest.CHANGE_EMAIL);
		}
	}

}
