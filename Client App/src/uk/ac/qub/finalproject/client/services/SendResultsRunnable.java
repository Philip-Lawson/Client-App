/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.io.IOException;

import uk.ac.qub.finalproject.calculationclasses.ResultsPacketList;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.app.Service;
import android.content.Context;
import android.provider.Settings.Secure;

/**
 * @author Phil
 *
 */
public class SendResultsRunnable extends RunnableClientTemplate {

	private ResultsPacketList resultsPacketList;
	private AbstractRequestHandler requestHandler;

	public SendResultsRunnable(Context context, Service service,
			ResultsPacketList resultsPacketList) {
		super(context, service);
		this.resultsPacketList = resultsPacketList;
	}

	@Override
	protected void setup() {
		requestHandler = new ServerRequestHandler(context);
		workDB = new FileAndPrefStorage(context);
		
		workDB.logNetworkRequest(ClientRequest.PROCESS_RESULT);	
		resultsPacketList.setDeviceID(Secure.getString(
				context.getContentResolver(), Secure.ANDROID_ID));
	}

	@Override
	protected void communicateWithServer() throws IOException {

		output.reset();
		output.write(ClientRequest.PROCESS_RESULT);
		output.writeObject(resultsPacketList);
		output.flush();

	}

	@Override
	protected void processConnection() throws IOException {
		requestHandler.processRequest(input);
	}

	@Override
	protected void finish() {
		workDB.deleteNetworkRequest(ClientRequest.PROCESS_RESULT);
		service.stopSelf();
	}

}
