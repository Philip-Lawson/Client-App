/**
 * 
 */
package com.example.fgjkj;

import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import uk.ac.qub.finalproject.client.services.ClientRequest;
import uk.ac.qub.finalproject.client.services.RunnableClientTemplate;

/**
 * @author Phil
 *
 */
public class DeregisterUserThread extends RunnableClientTemplate {
	
	private boolean deregisterSuccess = false;
	private Handler handler;
	
	public DeregisterUserThread(){
		super();
	}
	
	public DeregisterUserThread(Context context, Handler handler){
		super(context);
		this.handler = handler;
	}

	/* (non-Javadoc)
	 * @see uk.ac.qub.finalproject.client.services.RunnableClientTemplate#communicateWithServer()
	 */
	@Override
	protected void communicateWithServer() throws IOException {
		String deviceID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		
		output.writeInt(ClientRequest.DEREGISTER_DEVICE);
		output.writeObject(deviceID);
		
		deregisterSuccess = input.readBoolean();

	}
	
	@Override
	protected void finish(){
		Message msg = Message.obtain();
		Bundle b = new Bundle();
		b.putBoolean(DeleteAccountFragment.DEREGISTRATION_SUCCESS, deregisterSuccess);
		msg.setData(b);
		handler.sendMessage(msg);
	}
	
	@Override
	protected void informUserConnectionUnsuccessful(){
		finish();
	}

}
