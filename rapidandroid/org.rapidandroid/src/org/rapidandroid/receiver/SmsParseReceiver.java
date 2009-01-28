/**
 * 
 */
package org.rapidandroid.receiver;


import java.util.Vector;

import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.content.translation.ParsedDataTranslator;

import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.parser.IParseResult;
import org.rapidsms.java.core.parser.service.ParsingService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009
 * 
 *          Second level broadcast receiver. The idea is upon a successful SMS
 *          message save, a separate receiver will be triggered to handle the
 *          actual parsing and processing of the message.
 * 
 */
public class SmsParseReceiver extends BroadcastReceiver {

	
	private String[] prefixes = null;
	private Form[] forms = null;
	private Context mContext = null;
	
	private void initLists() {		
		forms = ModelTranslator.getAllForms();
		prefixes = new String[forms.length];
		for(int i = 0; i < forms.length; i++) {
			prefixes[i] = forms[i].getPrefix();
		}		
	}
	
	private Form determineForm(String message) {
		int len = prefixes.length;
		for(int i = 0; i < len; i++) {
			String prefix = prefixes[i];
			if(message.startsWith(prefix+" ")){
				return forms[i];
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {		
		if(mContext == null) {
			mContext = context;
		}
		if(prefixes == null) {
			initLists();
		}
		// TODO Auto-generated method stub
		String body = intent.getStringExtra("body");
		int msgid = intent.getIntExtra("msgid", 0);
		
		Form form = determineForm(body);
		if(form == null) {
			return;
		} else {
			Vector<IParseResult> results = ParsingService.ParseMessage(form, body);
			ParsedDataTranslator.InsertFormData(context, form, msgid, results);
		}		
	}
}
