/**
 * 
 */
package org.rapidandroid.view;

import org.rapidandroid.R;

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
public class MessageViewAdapter extends BaseAdapter {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */

	private Context context;
	private int mRLayoutID;

	// private List<String> hack_text;
	private String[] hackmonitors = { "Henry IV", "Henry V", "Henry VIII",
			"Richard II", "Richard III", "Merchant of Venice", "Othello",
			"King Lear" };

	private String[] hackdates = { "1/1/2009", "1/2/2009", "1/3/2009",
			"1/4/2009", "1/5/2009", "1/6/2009", "1/7/2009", "1/8/2009", };

	private String[] hackparse = { "0|123412351|something|null|1234210",
			"2|1|null|null|0", "3|3242411|zbadfhjsfgjkfghk|asdfasf|0",
			"4|1123|something|46745672|0",
			"5|1dfsdfsdf|something|asvasdfqwer|01252134",
			"6|1|asdfqwer|null|0", "7|1|something|asdfwqer|012351234",
			"8|123124124|4312341234|null|0", };

	private String[] hackraw = { "Now is the winter of our discontent",
			"Made glorious summer by this sun of York;",
			"And all the clouds that lour'd upon our house",
			"In the deep bosom of the ocean buried.",
			"Now are our brows bound with victorious wreaths;",
			"Our bruised arms hung up for monuments;",
			"Our stern alarums changed to merry meetings,",
			"Our dreadful marches to delightful measures.", };

	public MessageViewAdapter(Context thecontext, int rlayout_id) {
		context = thecontext;
		mRLayoutID = rlayout_id;
		// dmyung - hack job, we're storing stuff in here
	}

	/**
	 * Convenience method to expand or hide the dialogue
	 */
	public void setExpanded(boolean expanded) {
		// mParsed.setVisibility(expanded ? VISIBLE : GONE);
		// mRaw.setVisibility(expanded ? VISIBLE : GONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		// TODO Auto-generated method stub
		return hackmonitors.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return hackraw[position];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflate = LayoutInflater.from(context);
		View v = inflate.inflate(mRLayoutID, parent);
		// public static final int txv_monitor=0x7f050010;
		// public static final int txv_parsed=0x7f050013;
		// public static final int txv_rawmessage=0x7f050014;
		// public static final int txv_timestamp=0x7f050011;
		TextView txv_monitor = (TextView) v.findViewById(R.id.txv_monitor);
		TextView txv_parsed = (TextView) v.findViewById(R.id.txv_parsed);
		TextView txv_rawmessage = (TextView) v
				.findViewById(R.id.txv_rawmessage);
		TextView txv_timestamp = (TextView) v.findViewById(R.id.txv_timestamp);

		if (txv_monitor != null) {
			txv_monitor.setText(this.hackmonitors[position]);
		}

		if (txv_parsed != null) {
			txv_parsed.setText(this.hackparse[position]);
		}

		if (txv_rawmessage != null) {
			txv_rawmessage.setText(this.hackraw[position]);
		}

		if (txv_timestamp != null) {
			txv_timestamp.setText(this.hackdates[position]);
		}
		return v;

	}

}
