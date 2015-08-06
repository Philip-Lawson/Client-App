/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.io.IOException;
import java.io.ObjectInputStream;

import android.content.Context;
import android.content.Intent;

/**
 * @author Phil
 *
 */
public abstract class AbstractRequestHandler {

	protected Context context;

	public AbstractRequestHandler(Context context) {
		this.context = context;
	}

	public void processRequest(ObjectInputStream input) throws IOException {
		int serverRequest = input.readInt();

		switch (serverRequest) {
		case (ServerRequest.BECOME_DORMANT):
			becomeDormant();
			break;
		case (ServerRequest.LOAD_PROCESSING_CLASS):
			cancelDormantService();
			loadProcessingClass(input);
			break;
		case (ServerRequest.PROCESS_WORK_PACKETS):
			cancelDormantService();
			processWorkPackets(input);
			break;
		}

	}

	protected abstract void becomeDormant();

	protected abstract void loadProcessingClass(ObjectInputStream input);

	protected abstract void processWorkPackets(ObjectInputStream input);
	
	private void cancelDormantService(){
		Intent intent = new Intent(context, DormantService.class);
		context.stopService(intent);
	}

}
