/**
 * 
 */
package org.rapidandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author dmyung
 * @created Jan 15, 2009
 */
public class SmsParseReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String body = intent.getStringExtra("body");
		body.length();
	}
}
