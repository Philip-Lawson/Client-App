/**
 * 
 */
package uk.ac.qub.finalproject.client.services;

import java.io.IOException;
import java.io.ObjectInputStream;

import android.content.Context;
import android.content.Intent;

/**
 * This is an abstract base class used to handle requests from the server. It
 * requires use of the application context to complete tasks such as storing
 * files received from the server and starting new services. It is recommended
 * that implementations do not override the process request method, but that is
 * the responsibility of the implementer.
 * 
 * @author Phil
 *
 */
public abstract class AbstractRequestHandler {

	/**
	 * The application context. This is needed to interface with the filing
	 * system and to start and stop application services.
	 */
	protected Context context;

	public AbstractRequestHandler(Context context) {
		this.context = context;
	}

	/**
	 * Processes requests from the server. Note that this can only receive
	 * information from the server, it cannot send information back.
	 * 
	 * @param input
	 *            the input from the connection to the server.
	 * @throws IOException
	 */
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

	/**
	 * Responds to a request from the server to move the application to a
	 * dormant state.
	 */
	protected abstract void becomeDormant();

	/**
	 * Processes a request from the server to load the processing class onto the
	 * device.
	 * 
	 * @param input
	 */
	protected abstract void loadProcessingClass(ObjectInputStream input);

	/**
	 * Processes a request from the server to receive and process a list of work
	 * packets.
	 * 
	 * @param input
	 */
	protected abstract void processWorkPackets(ObjectInputStream input);

	private void cancelDormantService() {
		Intent intent = new Intent(context, DormantService.class);
		context.stopService(intent);
	}

}
