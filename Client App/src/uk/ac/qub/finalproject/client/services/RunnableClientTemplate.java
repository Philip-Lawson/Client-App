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
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;

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

	private boolean networkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo().isConnected();
	}

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

	protected abstract void communicateWithServer() throws IOException;

	protected void processConnection() throws IOException {

	}

	protected void finish() {

	}
	
	protected void informUserConnectionUnsuccessful(){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
