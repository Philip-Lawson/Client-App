/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.io.IOException;

import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import android.content.Context;

/**
 * @author Phil
 *
 */
public class LoadProcessingClassRunnable extends RunnableClientTemplate {
	
	private AbstractRequestHandler requestHandler;

	public LoadProcessingClassRunnable(Context context){
		super(context);
	}
	
	@Override
	protected void setup(){
		workDB = new FileAndPrefStorage(context);
		requestHandler = new ServerRequestHandler(context);		
		workDB.logNetworkRequest(ClientRequest.REQUEST_PROCESSING_CLASS);		
	}
	/* (non-Javadoc)
	 * @see uk.ac.qub.finalproject.client.services.Client#communicateWithServer()
	 */
	@Override
	protected void communicateWithServer() throws IOException {
		output.reset();
		output.writeInt(ClientRequest.REQUEST_PROCESSING_CLASS);
		output.flush();
	}
	
	@Override
	protected void processConnection() throws IOException {
		requestHandler.processRequest(input);
	}

}
