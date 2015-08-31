/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.io.IOException;

import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.app.Service;
import android.content.Context;
import android.provider.Settings.Secure;

/**
 * @author Phil
 *
 */
public class RequestWorkPacketRunnable extends RunnableClientTemplate {

	
	private AbstractRequestHandler requestHandler;

	public RequestWorkPacketRunnable(Context context, Service service) {
		super(context, service);
	}

	@Override
	public void setup() {
		workDB = new FileAndPrefStorage(context);
		requestHandler = new ServerRequestHandler(context);
		workDB.logNetworkRequest(ClientRequest.REQUEST_WORK_PACKET);
	}

	@Override
	protected void communicateWithServer() throws IOException {
		String deviceID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		
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
		service.stopSelf();
	}

}
