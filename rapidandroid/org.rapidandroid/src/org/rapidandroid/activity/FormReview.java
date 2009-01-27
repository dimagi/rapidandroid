/**
 * 
 */
package org.rapidandroid.activity;

import org.rapidandroid.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * 
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 12, 2009
 * 
 *          Activity window for reviewing the data in a Form. This may be phased
 *          out in favor of building up the capabilities of the Dashboard's
 *          listview.
 * 
 */
public class FormReview extends ListActivity { // this could totally be a list
												// activity

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

	private static final int MENU_VIEW_REPORTS = Menu.FIRST;
	private static final int MENU_DONE = Menu.FIRST + 1;

	private String mDialogMessage = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// dmyung 1/11/2009 :: changed to list activity, so we are not rendering
		// from an xml file at the moment.
		// setContentView(R.layout.form_review);
		// ListView lsv = (ListView) findViewById(R.id.lsv_messages);
		// ParsedMessageViewAdapter msgAdapter = new
		// ParsedMessageViewAdapter(lsv.getContext(), R.layout.message_view);
		// ParsedMessageViewAdapter msgAdapter = new ParsedMessageViewAdapter(this,
		// R.layout.message_view);
		// lsv.setAdapter(msgAdapter);
		// lsv.setAdapter(spc);

		// right now we're using the API example of the Speech List for a simple
		// customizable List elements.
		SpeechListAdapter spc = new SpeechListAdapter(this);
		setListAdapter(spc);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_VIEW_REPORTS, 0, R.string.dashboard_menu_show_reports);
		menu.add(0, MENU_DONE, 0, R.string.menu_done);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_VIEW_REPORTS:
			mDialogMessage = "TODO:  Go to reports activity";
			showDialog(0);
			return true;
		case MENU_DONE:
			finish();
			return true;
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);

		return new AlertDialog.Builder(FormReview.this)
				.setTitle("Debug Dialog").setMessage(mDialogMessage)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						/* User clicked OK so do some stuff */
					}
				}).create();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		((SpeechListAdapter) getListAdapter()).toggle(position);
	}

	/**
	 * A sample ListAdapter that presents content from arrays of speeches and
	 * text.
	 * 
	 */
	private class SpeechListAdapter extends BaseAdapter {
		public SpeechListAdapter(Context context) {
			mContext = context;
		}

		/**
		 * The number of items in the list is determined by the number of
		 * speeches in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			return mTitles.length;
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
//			SpeechView sv;
//			if (convertView == null) {
//				sv = new SpeechView(mContext, mTitles[position],
//						mDialogue[position], mExpanded[position]);
//			} else {
//				sv = (SpeechView) convertView;
//				sv.setTitle(mTitles[position]);
//				sv.setDialogue(mDialogue[position]);
//				sv.setExpanded(mExpanded[position]);
//			}
//
//			return sv;
			return null;
		}

		public void toggle(int position) {
			mExpanded[position] = !mExpanded[position];
			notifyDataSetChanged();
		}

		/**
		 * Remember our context so we can use it when constructing views.
		 */
		private Context mContext;

		/**
		 * Our data, part 1.
		 */
		private String[] mTitles = { ">>> Jon Doe (14:00) [Success]",
				"<<< Jon Doe (14:01) [Thanks]",
				">>> 26097755444 (15:00) [3/5]",
				">>> 26097655444 (15:30) [0/5]",
				"<<< 26097655444 (15:31) [Bad]",
				">>> 26097655443 (15:50) [3/4]",
				">>> 26097655442 (16:10) [4/5]",
				">>> 26097655441 (16:30) [4/5]", };

		/**
		 * Our data, part 2.
		 */
		private String[] mDialogue = {

				"Which fourteen hundred years ago were nail'd"
						+ "For our advantage on the bitter cross."
						+ "But this our purpose now is twelve month old,"
						+ "And bootless 'tis to tell you we will go:"
						+ "Therefore we meet not now. Then let me hear"
						+ "Of you, my gentle cousin Westmoreland,"
						+ "What yesternight our council did decree"
						+ "In forwarding this dear expedience.",

				"Must be the mistress to this theoric:"
						+ "Which is a wonder how his grace should glean it,"
						+ "Since his addiction was to courses vain,"
						+ "His companies unletter'd, rude and shallow,"
						+ "His hours fill'd up with riots, banquets, sports,"
						+ "And never noted in him any study,"
						+ "Any retirement, any sequestration"
						+ "From open haunts and popularity.",

				"Be sad, as we would make ye: think ye see"
						+ "The very persons of our noble story"
						+ "As they were living; think you see them great,"
						+ "And follow'd with the general throng and sweat"
						+ "Of thousand friends; then in a moment, see"
						+ "How soon this mightiness meets misery:"
						+ "And, if you can be merry then, I'll say"
						+ "A man may weep upon his wedding-day.",

				"Thou art a traitor and a miscreant,"
						+ "Too good to be so and too bad to live,"
						+ "Since the more fair and crystal is the sky,"
						+ "The uglier seem the clouds that in it fly."
						+ "Once more, the more to aggravate the note,"
						+ "With a foul traitor's name stuff I thy throat;"
						+ "And wish, so please my sovereign, ere I move,"
						+ "What my tongue speaks my right drawn sword may prove.",

				"In deadly hate the one against the other:"
						+ "And if King Edward be as true and just"
						+ "As I am subtle, false and treacherous,"
						+ "This day should Clarence closely be mew'd up,"
						+ "About a prophecy, which says that 'G'"
						+ "Of Edward's heirs the murderer shall be."
						+ "Dive, thoughts, down to my soul: here"
						+ "Clarence comes.",

				"us, do we not die? and if you wrong us, shall we not"
						+ "revenge? If we are like you in the rest, we will"
						+ "resemble you in that. If a Jew wrong a Christian,"
						+ "what is his humility? Revenge. If a Christian"
						+ "wrong a Jew, what should his sufferance be by"
						+ "Christian example? Why, revenge. The villany you"
						+ "teach me, I will execute, and it shall go hard but I"
						+ "will better the instruction.",

				"power and corrigible authority of this lies in our"
						+ "wills. If the balance of our lives had not one"
						+ "scale of reason to poise another of sensuality, the"
						+ "blood and baseness of our natures would conduct us"
						+ "to most preposterous conclusions: but we have"
						+ "reason to cool our raging motions, our carnal"
						+ "stings, our unbitted lusts, whereof I take this that"
						+ "you call love to be a sect or scion.",

				"You cataracts and hurricanoes, spout"
						+ "Till you have drench'd our steeples, drown'd the cocks!"
						+ "You sulphurous and thought-executing fires,"
						+ "Vaunt-couriers to oak-cleaving thunderbolts,"
						+ "Singe my white head! And thou, all-shaking thunder,"
						+ "Smite flat the thick rotundity o' the world!"
						+ "Crack nature's moulds, an germens spill at once,"
						+ "That make ingrateful man!" };

		/**
		 * Our data, part 3.
		 */
		private boolean[] mExpanded = { false, false, false, false, false,
				false, false, false };
	}
}
