package uk.ac.qub.finalproject.s40143289.client.views;

import java.io.IOException;

import uk.ac.qub.finalproject.calculationclasses.RegistrationPack;
import uk.ac.qub.finalproject.client.services.ClientRequest;
import uk.ac.qub.finalproject.client.services.RunnableClientTemplate;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * This class registers the client with the server based on the information
 * passed to it in the RegistrationPack. It communicates with the calling UI
 * thread by passing a boolean to the Handler that is passed in through the
 * constructor.
 * 
 * @author Phil
 *
 */
public class RegisterClientThread extends RunnableClientTemplate {

	private RegistrationPack registrationPack;
	private Handler handler;

	public RegisterClientThread() {
		super();
	}

	public RegisterClientThread(Context context) {
		super(context);
	}

	public RegisterClientThread(Context context,
			RegistrationPack registrationPack, Handler handler) {
		super(context);
		this.registrationPack = registrationPack;
		this.handler = handler;
	}

	public void communicateWithServer() throws IOException {
		output.reset();
		output.writeInt(ClientRequest.REGISTER);
		output.writeObject(registrationPack);
		output.flush();
	}

	@Override
	public void processConnection() throws IOException {
		boolean registrationSuccessful = input.readBoolean();

		Message msg = Message.obtain();
		Bundle b = new Bundle();
		b.putBoolean(RegisterPage.REGISTRATION_SUCCESS, registrationSuccessful);
		msg.setData(b);
		handler.sendMessage(msg);
	}

	@Override
	protected void informUserConnectionUnsuccessful() {
		boolean registrationUnsuccessful = false;

		Message msg = Message.obtain();
		Bundle b = new Bundle();
		b.putBoolean(RegisterPage.REGISTRATION_SUCCESS,
				registrationUnsuccessful);
		msg.setData(b);
		handler.sendMessage(msg);
	}

}
