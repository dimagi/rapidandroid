/*
 *    rapidandroid - SMS gateway for the android platform
 *    Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
/**
 * 
 */
package org.rapidandroid.receiver;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import org.rapidandroid.ApplicationGlobals;
import org.rapidandroid.content.translation.MessageTranslator;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.content.translation.ParsedDataTranslator;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Monitor;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.service.ParsingService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Second level broadcast receiver. The idea is upon a successful SMS message
 * save, a separate receiver will be triggered to handle the actual parsing and
 * processing of the message.
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009
 * 
 */
public class SmsParseReceiver extends BroadcastReceiver {

	private static String[] prefixes = null;
	private static Form[] forms = null;
	
	
	

	// private Context mContext = null;

	public synchronized static void initFormCache() {
		forms = ModelTranslator.getAllForms();
		prefixes = new String[forms.length];
		for (int i = 0; i < forms.length; i++) {
			prefixes[i] = forms[i].getPrefix();
		}
	}

	private Form determineForm(String message) {
		int len = prefixes.length;
		for (int i = 0; i < len; i++) {
			String prefix = prefixes[i];
			if (message.toLowerCase().trim().startsWith(prefix + " ")) {
				return forms[i];
			}
		}
		return null;
	}

	/**
	 * Upon message receipt, determine the form in question, then call the
	 * corresponding parsing logic.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		ApplicationGlobals.initGlobals(context);
	
		if (prefixes == null) {
			initFormCache(); // profiler shows us that this is being called
								// frequently on new messages.
		}
		// TODO Auto-generated method stub
		String body = intent.getStringExtra("body");

		if (body.startsWith("notifications@dimagi.com /  / ")) {
			body = body.replace("notifications@dimagi.com /  / ", "");
			Log.d("SmsParseReceiver", "Debug, snipping out the email address");
		}

		int msgid = intent.getIntExtra("msgid", 0);

		Form form = determineForm(body);
		if (form == null) {			
			if (ApplicationGlobals.doReplyOnFail()) {
				Intent broadcast = new Intent("org.rapidandroid.intents.SMS_REPLY");
				broadcast.putExtra(SmsReplyReceiver.KEY_DESTINATION_PHONE, intent.getStringExtra("from"));
				broadcast.putExtra(SmsReplyReceiver.KEY_MESSAGE, ApplicationGlobals.getParseFailText());
				context.sendBroadcast(broadcast);
			}
			return;
		} else {
			Monitor mon = MessageTranslator.GetMonitorAndInsertIfNew(context, intent.getStringExtra("from"));
			// if(mon.getReplyPreference()) {
			if (ApplicationGlobals.doReplyOnParse()) {
				// for debug purposes, we'll just ack every time.
				Intent broadcast = new Intent("org.rapidandroid.intents.SMS_REPLY");
				broadcast.putExtra(SmsReplyReceiver.KEY_DESTINATION_PHONE, intent.getStringExtra("from"));
				broadcast.putExtra(SmsReplyReceiver.KEY_MESSAGE, ApplicationGlobals.getParseSuccessText());
				context.sendBroadcast(broadcast);
			}
			Vector<IParseResult> results = ParsingService.ParseMessage(form, body);
			ParsedDataTranslator.InsertFormData(context, form, msgid, results);
		}
	}
}
