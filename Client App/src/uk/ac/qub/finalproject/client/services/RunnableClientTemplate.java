package uk.ac.qub.finalproject.client.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import uk.ac.qub.finalproject.client.persistence.DataStorage;
import uk.ac.qub.finalproject.client.views.R;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * This abstract class uses the template method pattern to allow subclasses to
 * implement their own behaviours as appropriate while implementing the common
 * methods needed to connect to the server. It also allows for a consistent
 * behaviour among all subclasses. <br>
 * </br> Note that only the method to communicate with the server must be
 * implemented.
 * 
 * @author Phil
 *
 */
public abstract class RunnableClientTemplate implements Runnable {

	private static final int PORT_NUMBER = 12346;
	private static final String HOST = "10.0.2.2";
	protected ObjectInputStream input;
	protected ObjectOutputStream output;

	protected Context context;
	protected Service service;
	protected DataStorage workDB;
	private Socket client;

	public RunnableClientTemplate() {

	}

	public RunnableClientTemplate(Context context) {
		this.context = context;
	}

	public RunnableClientTemplate(Context context, Service service) {
		this.context = context;
		this.service = service;
	}

	/**
	 * Checks to see if the network is available prior to attempting to
	 * communicate with the server. Note that this method is replicated in the
	 * network info broadcast receiver.
	 * 
	 * @return
	 */
	private boolean networkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		int networkInfo = connectivityManager.getActiveNetworkInfo().getType();

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String networkKey = context.getString(R.string.wifi_key);
		String wifiOnly = context.getString(R.string.network_description_wifi);

		if (pref.getString(networkKey, wifiOnly).equals(wifiOnly)) {
			return networkInfo == ConnectivityManager.TYPE_WIFI;
		} else {
			return networkInfo == ConnectivityManager.TYPE_WIFI
					|| networkInfo == ConnectivityManager.TYPE_MOBILE;
		}
	}

	/**
	 * Set up necessary objects and conditions before communicating with the
	 * server.
	 */
	protected void setup() {

	}

	private void connectToServer() throws UnknownHostException, IOException {
		// SSLSocketFactory factory = (SSLSocketFactory)
		// SSLSocketFactory.getDefault();
		// client = (SSLSocket)
		// factory.createSocket(InetAddress.getByName(HOST), PORT_NUMBER);
		client = new Socket(InetAddress.getByName(HOST), PORT_NUMBER);

	}

	private void getStreams() throws IOException {
		input = new ObjectInputStream(new BufferedInputStream(
				client.getInputStream()));
		output = new ObjectOutputStream(new BufferedOutputStream(
				client.getOutputStream()));
		output.flush();
	}

	/**
	 * Send a request type to the server along with the appropriate
	 * information/data.
	 * 
	 * @throws IOException
	 */
	protected abstract void communicateWithServer() throws IOException;

	/**
	 * Process information from the server.
	 * 
	 * @throws IOException
	 */
	protected void processConnection() throws IOException {

	}

	/**
	 * Clean up after all networking actions are completed.
	 */
	protected void finish() {

	}

	/**
	 * Initiate an action to inform the user that the connection was
	 * unsuccessful. This could be due to a network being unavailable, or the
	 * user's preferences not permitting a data connection.
	 */
	protected void informUserConnectionUnsuccessful() {

	}

	@Override
	public void run() {
		try {
			setup();

			if (networkAvailable()) {
				connectToServer();
				getStreams();
				communicateWithServer();
				processConnection();
				finish();
			} else {
				informUserConnectionUnsuccessful();
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {

		} finally {
			try {
				if (output != null)
					output.close();
			} catch (IOException e) {

			}

			try {
				if (input != null)
					input.close();
			} catch (IOException e) {

			}

			try {
				if (client != null)
					client.close();
			} catch (IOException e) {

			}
		}

	}

}
