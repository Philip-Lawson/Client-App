package com.example.fgjkj;

import java.io.IOException;

import uk.ac.qub.finalproject.client.services.ClientRequest;
import uk.ac.qub.finalproject.client.services.RunnableClientTemplate;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class RegisterClientThread extends RunnableClientTemplate {

	private RegistrationPack registrationPack;
	private Handler handler;

	public RegisterClientThread() {
		super();
	}

	public RegisterClientThread(Context context) {
		super(context);
	}

	public RegisterClientThread(Context context, RegistrationPack registrationPack,
			Handler handler) {
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
		b.putBoolean(MainActivity.REGISTRATION_SUCCESS, registrationSuccessful);
		msg.setData(b);
		handler.sendMessage(msg);
	}
	
	@Override
	protected void informUserConnectionUnsuccessful(){
		boolean registrationUnsuccessful = false;
		
		Message msg = Message.obtain();
		Bundle b = new Bundle();
		b.putBoolean(MainActivity.REGISTRATION_SUCCESS, registrationUnsuccessful);
		msg.setData(b);
		handler.sendMessage(msg);
	}

}
