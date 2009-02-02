package org.rapidandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Feb 2, 2009 Summary:
 */
public class SmsReplyReceiver extends BroadcastReceiver {

	public static final String KEY_DESTINATION_PHONE = "tophone";
	public static final String KEY_MESSAGE = "msg";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		SmsManager smgr = SmsManager.getDefault();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.containsKey(KEY_DESTINATION_PHONE) && extras.containsKey(KEY_MESSAGE)) {
				String destinationAddr = extras.getString(KEY_DESTINATION_PHONE);
				String mesg = extras.getString(KEY_MESSAGE);
				smgr.sendTextMessage(destinationAddr, null, mesg, null, null);				
			}
		}
	}

}
