/**
 * 
 */
package uk.ac.qub.finalproject.s40143289.client.views;

import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import uk.ac.qub.finalproject.client.services.ClientRequest;
import uk.ac.qub.finalproject.client.services.RunnableClientTemplate;

/**
 * This runnable sends a message to the server asking it to delete the device
 * from its records. It receives a boolean determining if the request was
 * successful. Once it receives the request it sends a call back to the handler
 * informing it of the success or failure of the deletion.
 * 
 * @author Phil
 *
 */
public class DeregisterUserThread extends RunnableClientTemplate {

	private boolean deregisterSuccess = false;
	private Handler handler;

	public DeregisterUserThread() {
		super();
	}

	public DeregisterUserThread(Context context, Handler handler) {
		super(context);
		this.handler = handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.qub.finalproject.client.services.RunnableClientTemplate#
	 * communicateWithServer()
	 */
	@Override
	protected void communicateWithServer() throws IOException {
		String deviceID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);		

		output.reset();
		output.writeInt(ClientRequest.DEREGISTER_DEVICE);
		output.writeObject(deviceID);
		output.flush();

		deregisterSuccess = input.readBoolean();

	}

	@Override
	protected void finish() {
		// sends a callback to the caller page
		// with a boolean flag confirming if the
		// account delete was successful
		Message msg = Message.obtain();
		Bundle b = new Bundle();
		b.putBoolean(MainPage.DEREGISTRATION_SUCCESS, deregisterSuccess);
		msg.setData(b);
		handler.sendMessage(msg);
	}

	@Override
	protected void informUserConnectionUnsuccessful() {
		finish();
	}

}
