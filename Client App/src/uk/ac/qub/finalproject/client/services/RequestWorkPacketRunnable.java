/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.io.IOException;

import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import uk.ac.qub.finalproject.s40143289.client.views.R;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;

/**
 * This networking runnable requests another work packet list from the server.
 * Note that when it receives the work packet it does not start processing. It
 * is the responsibility of the battery monitor service to start processing when
 * it is appropriate.
 * 
 * @author Phil
 *
 */
public class RequestWorkPacketRunnable extends RunnableClientTemplate {

	/**
	 * This handles the response from the server. This is needed as the server
	 * could respond in various different ways.
	 */
	private AbstractRequestHandler requestHandler;

	public RequestWorkPacketRunnable(Context context, Service service) {
		super(context, service);
	}

	@Override
	public void setup() {
		workDB = FileAndPrefStorage.getInstance(context);
		requestHandler = new ServerRequestHandler(context);
		workDB.logNetworkRequest(ClientRequest.REQUEST_WORK_PACKET);
	}

	@Override
	protected void communicateWithServer() throws IOException {
		String deviceID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		output.reset();
		output.writeInt(ClientRequest.REQUEST_WORK_PACKET);
		output.writeObject(deviceID);
		output.flush();
	}

	@Override
	public void processConnection() throws IOException {
		requestHandler.processRequest(input);
	}

	@Override
	public void finish() {
		workDB.deleteNetworkRequest(ClientRequest.REQUEST_WORK_PACKET);
		setHasBegunProcessing();
		service.stopSelf();
	}

	private void setHasBegunProcessing() {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		edit.putBoolean(context.getString(R.string.processing_started_key),
				true).apply();
	}

}
