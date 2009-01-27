/**
 * 
 */
package org.rapidandroid.view.adapter;

import java.util.HashMap;
import java.util.Set;

import org.rapidandroid.R;
import org.rapidandroid.view.ParsedMessageView;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.parser.IParseResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 9, 2009
 * 
 *          Pulled from the Android API examples for an example of an adapter
 *          with a custom view for a listview.
 * 
 * 
 */
public class ParsedMessageViewAdapter extends BaseAdapter {
	
	/**
	 * Remember our context so we can use it when constructing views.
	 */
	private Context mContext;
	private Message[] messages;
	private Form mForm;
	
	private boolean[] mExpanded;
	
	HashMap<Message,IParseResult[]> mParsedMessages;
	
	public ParsedMessageViewAdapter(Context context, Form form, HashMap<Message,IParseResult[]> parsedMessages) {
		mContext = context;
		mForm = form;		
		
		messages = (Message[])parsedMessages.keySet().toArray(new Message[parsedMessages.keySet().size()]);		
		mExpanded = new boolean[messages.length];
		int len = messages.length;		
		for(int i = 0; i < len; i++) {
			mExpanded[i] = false;		 
		}
		
		mParsedMessages = parsedMessages;
	}

	/**
	 * The number of items in the list is determined by the number of
	 * speeches in our array.
	 * 
	 * @see android.widget.ListAdapter#getCount()
	 */
	public int getCount() {
		return messages.length;
		
	}

	/**
	 * Since the data comes from an array, just returning the index is
	 * sufficent to get at the data. If we were using a more complex data
	 * structure, we would return whatever object represents one row in the
	 * list.
	 * 
	 * @see android.widget.ListAdapter#getItem(int)
	 */
	public Object getItem(int position) {
		return position;
	}

	/**
	 * Use the array index as a unique id.
	 * 
	 * @see android.widget.ListAdapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Make a SpeechView to hold each row.
	 * 
	 * @see android.widget.ListAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ParsedMessageView mv;
		Message mesg = messages[position];
		IParseResult[] parsedResults = mParsedMessages.get(mesg);
		
		if (convertView == null) {			
			mv = new ParsedMessageView(mContext, mForm, mesg, parsedResults, false);			
		} else {
			mv = (ParsedMessageView)convertView;
			mv.setData(mesg,parsedResults);
			mv.setExpanded(mExpanded[position]);			
		}
		return mv;
	}

	public void toggle(int position) {
		mExpanded[position] = !mExpanded[position];
		notifyDataSetChanged();
	}




}
