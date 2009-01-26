/**
 * 
 */
package org.rapidandroid.activity;

import java.util.HashMap;

import org.rapidandroid.ActivityConstants;
import org.rapidandroid.R;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.content.translation.ParsedDataTranslator;
import org.rapidandroid.data.RapidSmsDataDefs;
import org.rapidandroid.view.adapter.ParsedMessageViewAdapter;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;
import org.rapidsms.java.core.parser.IParseResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 9, 2009
 * 
 *          Main entry point activity for RapidAndroid. It is a
 *          simple view with a pulldown for for form type, and a listview of
 *          messages below that pertain to that message.
 * 
 */
public class Dashboard extends Activity {
	private String dialogMessage = "";

	private boolean mFormSelected = false;
	private int mSelectedFormId = -1;
	private int mMessageSelected = -1;

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_VIEW = 2; // this and ACTIVITY_REPORTS
	// don't really need to be
	// reported back to this view.
	private static final int ACTIVITY_REPORTS = 3;

	private static final int MENU_CREATE_ID = Menu.FIRST;
	private static final int MENU_EDIT_ID = Menu.FIRST + 1;
	private static final int MENU_VIEW_ID = Menu.FIRST + 2;
	private static final int MENU_SHOW_REPORTS = Menu.FIRST + 3;
	// private static final int MENU_EXIT = Menu.FIRST + 3; //waitaminute, we
	// don't want to exit this thing, do we?

	private static final int CONTEXT_ITEM_TEST1 = ContextMenu.FIRST;
	private static final int CONTEXT_ITEM_TEST2 = ContextMenu.FIRST + 1;
	private static final int CONTEXT_ITEM_TEST3 = ContextMenu.FIRST + 2;
	private static final int CONTEXT_ITEM_TEST4 = ContextMenu.FIRST + 3;

	
	private Form[] mAllForms;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		if (savedInstanceState != null) {
			String from = savedInstanceState.getString("from");
			String body = savedInstanceState.getString("body");
			dialogMessage = "SMS :: " + from + " : " + body;
			showDialog(160);
		}

		this.loadFormSpinner();

		// Set the event listeners for the spinner and the listview
		Spinner spin_forms = (Spinner) findViewById(R.id.cbx_forms);
		spin_forms
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View theview, int position, long rowid) {
						spinnerItemSelected(position);
					}

					public void onNothingSelected(AdapterView<?> parent) {
						// blow away the listview's items
						mFormSelected = false;
						mSelectedFormId = -1;
						loadMessagesForForm(null);
					}
				});

		// add some events to the listview
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);

		
		
		// bind a context menu
		lsv
				.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.add(0, CONTEXT_ITEM_TEST1, 0, "Context 1");
						menu.add(0, CONTEXT_ITEM_TEST2, 0, "Context 2");
					}
				});
		lsv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long row) {
				((ParsedMessageViewAdapter)adapter.getAdapter()).toggle(position);
				
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		Bundle extras = null;
		if (intent != null) {
			extras = intent.getExtras(); // right now this is a case where we
			// don't do much activity back and
			// forth

		}

		switch (requestCode) {
		case ACTIVITY_CREATE:
			// we should do an update of the view
			dialogMessage = "Activity Done";
			showDialog(11);
			break;
		case ACTIVITY_EDIT:
			dialogMessage = "Activity Done";
			showDialog(12);
			break;
		case ACTIVITY_VIEW:
			dialogMessage = "Activity Done";
			showDialog(13);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// add images:
		// http://developerlife.com/tutorials/?p=304
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CREATE_ID, 0, R.string.dashboard_menu_create);
		menu.add(0, MENU_EDIT_ID, 0, R.string.dashboard_menu_edit);
		menu.add(0, MENU_VIEW_ID, 0, R.string.dashboard_menu_view);
		menu.add(0, MENU_SHOW_REPORTS, 0, R.string.dashboard_menu_show_reports);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_CREATE_ID:
			// showDialog(MENU_CREATE_ID); //debug, we'll need to spawn the
			// activities after this
			StartFormEditActivity(true);
			return true;
		case MENU_EDIT_ID:
			// showDialog(MENU_EDIT_ID); //debug, we'll need to spawn the
			// activities after this
			StartFormEditActivity(false);
			return true;
		case MENU_VIEW_ID:
			// showDialog(MENU_VIEW_ID); //debug, we'll need to spawn the
			// activities after this
			if (mSelectedFormId != -1) {
				StartFormViewerActivity(this.mAllForms[mSelectedFormId].getFormId());
			} else {
				showDialog(9999);
			}
			return true;
		case MENU_SHOW_REPORTS:

			// "content://sms/undelivered"
			// 01-12 20:57:20.783: DEBUG/SmsProvider(85): insert
			// url=content://sms/inbox, match=2
			// Uri uriSms = Uri.parse("content://sms/undelivered");
			/*
			 * public String strUriInbox = "content://sms/inbox";//SMS_INBOX:1
			 * public String strUriFailed =
			 * "content://sms/failed";//SMS_FAILED:2 public String strUriQueued
			 * = "content://sms/queued";//SMS_QUEUED:3 public String strUriSent
			 * = "content://sms/sent";//SMS_SENT:4 public String strUriDraft =
			 * "content://sms/draft";//SMS_DRAFT:5 public String strUriOutbox =
			 * "content://sms/outbox";//SMS_OUTBOX:6 public String
			 * strUriUndelivered = "content://sms/undelivered";//SMS_UNDELIVERED
			 * public String strUriAll = "content://sms/all";//SMS_ALL
			 * 
			 * public String strUriConversations =
			 * "content://sms/conversations";//you
			 * 
			 * content://sms/inbox/<id>
			 */
			Uri uriSms = Uri.parse("content://sms/conversations");
			Cursor c = getContentResolver().query(uriSms, null, null, null,
					null); // Sms.Inbox.CONTENT_URI,

			String[] columnames = c.getColumnNames();
			int count = c.getCount();
			c.moveToFirst();
			String id = c.getString(0);
			int thread_id = c.getInt(1);
			String addr = c.getString(2);
			String person = c.getString(3);
			String date = c.getString(4);
			String body = c.getString(11);
			// showDialog(MENU_VIEW_ID); //debug, we'll need to spawn the
			// activities after this
			// this.dialogMessage = "TODO:  Go to the reports activity";

			// getContentResolver().delete(Uri.parse("content://sms/conversations/"
			// + thread_id),null,null);
			// dmyung - oh baby this works. a bit extreme blowing away the
			// entire conversation. but at least we know (as of this writing
			// 1/12) that it works in the emulator.

			getContentResolver().delete(
					Uri.parse("content://sms/conversations/" + thread_id),
					null, null);
			dialogMessage = "row count: " + count;

			showDialog(9999);

			ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
			lsv.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, columnames));

			return true;
		}

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Flip the enabled status of menu items depending on selection of a
		// form
		super.onPrepareOptionsMenu(menu);

		MenuItem editMenu = menu.findItem(MENU_EDIT_ID);
		editMenu.setEnabled(mFormSelected);

		MenuItem viewMenu = menu.findItem(MENU_VIEW_ID);
		viewMenu.setEnabled(mFormSelected);

		MenuItem reportsMenu = menu.findItem(MENU_SHOW_REPORTS);
		reportsMenu.setEnabled(mFormSelected);

		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);

		return new AlertDialog.Builder(Dashboard.this).setTitle(
				"Menu Selection").setMessage(
				"Selected Menu Item: " + id + " " + dialogMessage)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked OK so do some stuff */
					}
				}).create();
	}

	// Start the form edit/create activity
	private void StartFormEditActivity(boolean isNew) {
//		Intent i;
//		if (isNew) {
//			i = new Intent(this, FormCreator.class);
//			startActivityForResult(i, ACTIVITY_CREATE);
//		} else {
//			i = new Intent(this, FormEditor.class);
//			i.putExtra(ActivityConstants.EDIT_FORM, mAllForms[mSelectedFormId].getFormId());
//			startActivityForResult(i, ACTIVITY_EDIT);
//		}
	}

	@Override
	// http://www.anddev.org/tinytutcontextmenu_for_listview-t4019.html
	// UGH, things changed from .9 to 1.0
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXT_ITEM_TEST1:
			// This is actually where the magic happens.
			// As we use an adapter view (which the ListView is)
			// We can cast item.getMenuInfo() to AdapterContextMenuInfo

			// To get the id of the clicked item in the list use menuInfo.id
			dialogMessage = "Context 1: List pos: " + menuInfo.position
					+ " id:" + menuInfo.id + " mMessageSelected: "
					+ mMessageSelected;

			showDialog(55);
			break;
		case CONTEXT_ITEM_TEST2:
			// This is actually where the magic happens.
			// As we use an adapter view (which the ListView is)
			// We can cast item.getMenuInfo() to AdapterContextMenuInfo
			// To get the id of the clicked item in the list use menuInfo.id
			dialogMessage = "Context 2: List pos: " + menuInfo.position
					+ " id:" + menuInfo.id + " mMessageSelected: "
					+ mMessageSelected;
			android.telephony.gsm.SmsManager smgr = android.telephony.gsm.SmsManager
					.getDefault();
			smgr.sendTextMessage("6176453236", null, "hello programmatic",
					null, null);
			showDialog(56);

			break;
		default:
			return super.onContextItemSelected(item);
		}
		return true;
	}

	private boolean applyMessageContextMenu(MenuItem item) {
		showDialog(item.getItemId());
		return true;
	}

	private void StartFormViewerActivity(int selectedFormId) {
		Intent i = new Intent(this, FormReview.class);
		i.putExtra("FormId", selectedFormId); // bad form, should use some
		// enum here
		startActivityForResult(i, ACTIVITY_VIEW);
	}

	// This is a call to the DB to get all the forms that this form can support.
	private void loadFormSpinner() {
		// The steps:
		// get the spinner control from the layouts
		Spinner spin_forms = (Spinner) findViewById(R.id.cbx_forms);
		// Get an array of forms from the DB
		// in the current iteration, it's mForms
		this.mAllForms = ModelTranslator.getAllForms(this
				.getApplicationContext());

		String[] monitors = new String[mAllForms.length + 2];
		

		for (int i = 0; i < mAllForms.length; i++) {
			monitors[i] = "Form: " + mAllForms[i].getFormName();
		}
		
		//add some special selections:
		monitors[monitors.length - 2] = "Show all Messages";
		monitors[monitors.length - 1] = "Show Monitors";
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, monitors);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// apply it to the spinner
		spin_forms.setAdapter(adapter);

	}

	private void spinnerItemSelected(int position) {
		

		if (position == mAllForms.length) {
			// if it's forms+1, then it's ALL messages
			mFormSelected = false;
			mSelectedFormId = -1;

		} else if (position == mAllForms.length + 1) {
			// then it's show all monitors
			mFormSelected = false;
			mSelectedFormId = -1;
			
			
		} else {

			// get the position, then reset the
			mFormSelected = true;
			mSelectedFormId = position;
			loadMessagesForForm(mAllForms[position]);
		}
		
	}
	
	// this is a call to the DB to update the ListView with the messages for a
	// selected form
	private void loadAllMessagesIntoList() {		
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
		lsv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, new String[] {"todo"}));
	}
	
	// this is a call to the DB to update the ListView with the messages for a
	// selected form
	private void loadMonitorsIntoList() {		
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
		lsv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, new String[] {"todo"}));
	}
	
	// this is a call to the DB to update the ListView with the messages for a
	// selected form
	private void loadMessagesForForm(Form chosenForm) {
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
		if (chosenForm == null) {
			lsv.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					new String[] { "Select a form" }));
		} 
		else {
			HashMap<Message,IParseResult[]> parsedHash = ParsedDataTranslator.getParsedMessagesForForm(this, chosenForm);
			if(parsedHash == null) {
				lsv.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,
						new String[] { "No messages" }));
			} else {
				lsv.setAdapter(new ParsedMessageViewAdapter(this,chosenForm, parsedHash));
			}
		}
	}

	

}
