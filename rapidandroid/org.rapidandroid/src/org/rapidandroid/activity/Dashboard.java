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
import org.rapidandroid.view.SingleRowHeaderView;
import org.rapidandroid.view.adapter.FormDataCursorAdapter;
import org.rapidandroid.view.adapter.MessageCursorAdapter;
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
	
	private SingleRowHeaderView headerView;
	private ParsedMessageViewAdapter summaryView; 
	private FormDataCursorAdapter rowView;
	private ArrayAdapter<String> emptyView;
	private MessageCursorAdapter messageCursorAdapter;
	
	
	private Form mChosenForm = null;
		
	private int mMessageSelected = -1;

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_FORM_REVIEW = 1;
	private static final int ACTIVITY_CHARTS = 2; // this and ACTIVITY_CHARTS
	
	private static final int MENU_CREATE_ID = Menu.FIRST;
	private static final int MENU_FORM_REVIEW_ID = Menu.FIRST + 1;
	private static final int MENU_CHARTS_ID = Menu.FIRST + 2;
	//private static final int MENU_SHOW_REPORTS = Menu.FIRST + 3;
	// private static final int MENU_EXIT = Menu.FIRST + 3; //waitaminute, we
	// don't want to exit this thing, do we?

	
	
	private static final int CONTEXT_ITEM_SUMMARY_VIEW = Menu.FIRST;
	private static final int CONTEXT_ITEM_TABLE_VIEW = Menu.FIRST + 1;
//	private static final int CONTEXT_ITEM_TEST3 = ContextMenu.FIRST + 2;
//	private static final int CONTEXT_ITEM_TEST4 = ContextMenu.FIRST + 3;

	private static final int LISTVIEW_MODE_SUMMARY_VIEW = 0;
	private static final int LISTVIEW_MODE_TABLE_VIEW = 1;
	//private static final int LISTVIEW_MODE_SUMMARY_VIEW = 0;
	
	private int formViewMode = 0;
	
	
	private Form[] mAllForms;
	
	
	@Override
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
						mChosenForm = null;
						populateListView();
					}
				});

		// add some events to the listview
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);

		lsv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		// bind a context menu
		lsv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, CONTEXT_ITEM_SUMMARY_VIEW, 0, "Summary View");
				menu.add(0, CONTEXT_ITEM_TABLE_VIEW, 0, "Table View");
			}
		});		
		
		lsv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long row) {
				if(adapter.getAdapter().getClass().equals(ParsedMessageViewAdapter.class) ) {
					((ParsedMessageViewAdapter) adapter.getAdapter()).toggle(position);
				}

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
			loadFormSpinner();
			break;
		case ACTIVITY_FORM_REVIEW:
			dialogMessage = "Activity Done";
			showDialog(12);
			break;
		case ACTIVITY_CHARTS:
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
		menu.add(0, MENU_FORM_REVIEW_ID, 0, R.string.dashboard_menu_edit);
		menu.add(0, MENU_CHARTS_ID, 0, R.string.dashboard_menu_view);
		//menu.add(0, MENU_SHOW_REPORTS, 0, R.string.dashboard_menu_show_reports);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_CREATE_ID:
			startActivityFormCreate();
			return true;
		case MENU_FORM_REVIEW_ID:
			startActivityFormReview();
			return true;
		case MENU_CHARTS_ID:
			startActivityChart(mChosenForm.getFormId());
			return true;
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Flip the enabled status of menu items depending on selection of a form
		super.onPrepareOptionsMenu(menu);

		boolean formOptionsEnabled = false;
		if(this.mChosenForm != null) {
			formOptionsEnabled = true;
		}
		
		MenuItem editMenu = menu.findItem(MENU_FORM_REVIEW_ID);
		editMenu.setEnabled(formOptionsEnabled);

		MenuItem viewMenu = menu.findItem(MENU_CHARTS_ID);
		viewMenu.setEnabled(formOptionsEnabled);

		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);

		return new AlertDialog.Builder(Dashboard.this).setTitle("Menu Selection")
														.setMessage("Selected Menu Item: " + id + " " + dialogMessage)
														.setPositiveButton("OK", new DialogInterface.OnClickListener() {
															public void onClick(DialogInterface dialog, int whichButton) {
																/*
																 * User clicked
																 * OK so do some
																 * stuff
																 */
															}
														}).create();
	}

	
	@Override
	// http://www.anddev.org/tinytutcontextmenu_for_listview-t4019.html
	// UGH, things changed from .9 to 1.0
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXT_ITEM_SUMMARY_VIEW:
			formViewMode = LISTVIEW_MODE_SUMMARY_VIEW;
			populateListView();
			break;
		case CONTEXT_ITEM_TABLE_VIEW:			
			formViewMode = LISTVIEW_MODE_TABLE_VIEW;
			populateListView();

			break;
		default:
			return super.onContextItemSelected(item);
		}
		return true;
	}

	// Start the form edit/create activity
	private void startActivityFormReview() {
		Intent i;
		
		i = new Intent(this, FormReviewer.class);
		i.putExtra(ActivityConstants.REVIEW_FORM, mChosenForm.getFormId());			
		startActivityForResult(i, ACTIVITY_FORM_REVIEW);	
	}	
	
	// Start the form edit/create activity
	private void startActivityFormCreate() {
		Intent i;
		i = new Intent(this, FormCreator.class);
		startActivityForResult(i, ACTIVITY_CREATE);		
	}	
	
	private void startActivityChart(int selectedFormId) {
		Intent i = new Intent(this, ChartData.class);
		i.putExtra(ActivityConstants.CHART_FORM, mChosenForm.getFormId());
		startActivityForResult(i, ACTIVITY_CHARTS);
	}


	// This is a call to the DB to get all the forms that this form can support.
	private void loadFormSpinner() {
		// The steps:
		// get the spinner control from the layouts
		Spinner spin_forms = (Spinner) findViewById(R.id.cbx_forms);
		// Get an array of forms from the DB
		// in the current iteration, it's mForms
		this.mAllForms = ModelTranslator.getAllForms();

		String[] monitors = new String[mAllForms.length + 2];
		

		for (int i = 0; i < mAllForms.length; i++) {
			monitors[i] = "Form: " + mAllForms[i].getFormName();
		}
		
		//add some special selections:
		monitors[monitors.length - 2] = "Show all Messages";
		monitors[monitors.length - 1] = "Show Monitors";
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, monitors);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// apply it to the spinner
		spin_forms.setAdapter(adapter);
	}

	private void spinnerItemSelected(int position) {
		if (position == mAllForms.length) {
			// if it's forms+1, then it's ALL messages			
			mChosenForm = null;
			loadAllMessagesIntoList();

		} else if (position == mAllForms.length + 1) {
			// then it's show all monitors			
			mChosenForm = null;
			loadAllMonitorsIntoList();
			
		} else {

			mChosenForm = mAllForms[position];
			// get the position, then reset the			
			populateListView();
		}
		
	}
	
	// this is a call to the DB to update the ListView with the messages for a
	// selected form
	private void loadAllMessagesIntoList() {		
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
		//lsv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, new String[] {"todo"}));
		if(rowView != null) {
			rowView.getCursor().close();
		}
		rowView = null;
		
		Cursor cursor = getContentResolver().query(RapidSmsDataDefs.Message.CONTENT_URI, null, null, null, null);		
		this.messageCursorAdapter = new MessageCursorAdapter(this, cursor);
		lsv.setAdapter(messageCursorAdapter);
	}
	
	
	// this is a call to the DB to update the ListView with the messages for a
	// selected form
	private void loadAllMonitorsIntoList() {		
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
		lsv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, new String[] {"todo"}));
	}
	
	// this is a call to the DB to update the ListView with the messages for a
	// selected form
	private void populateListView() {
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
		if (mChosenForm == null) {
			lsv.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					new String[] { "Select an item" }));
		} 
		else {
			HashMap<Message,IParseResult[]> parsedHash = ParsedDataTranslator.getParsedMessagesForForm(this, mChosenForm);
			if(parsedHash == null) {
				emptyView = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new String[] { "No Data" });
				lsv.setAdapter(emptyView);
			} else {
//				if (headerView == null) {
//					this.headerView = new SingleRowHeaderView(this, mChosenForm);
//					lsv.addHeaderView(headerView);
//				}
				
				if(this.formViewMode == Dashboard.LISTVIEW_MODE_SUMMARY_VIEW) {
										
//					headerView.setVisibility(View.INVISIBLE);
					this.summaryView = new ParsedMessageViewAdapter(this,mChosenForm, parsedHash);						
					if(rowView != null) {
						rowView.getCursor().close();
						rowView = null;
					}
					
					if(messageCursorAdapter != null) {
						messageCursorAdapter.getCursor().close();
						messageCursorAdapter = null;
					}
					
					
					
					lsv.setAdapter(summaryView);
				} else if(this.formViewMode == Dashboard.LISTVIEW_MODE_TABLE_VIEW) {
					
					Uri dataUri = Uri.parse(RapidSmsDataDefs.FormData.CONTENT_URI_PREFIX + mChosenForm.getFormId());
					Cursor cr = getContentResolver().query(dataUri, null,null,null,null);
					
					if(messageCursorAdapter != null) {
						messageCursorAdapter.getCursor().close();
						messageCursorAdapter = null;
					}
					
					
//					headerView.setVisibility(View.VISIBLE);
					rowView = new FormDataCursorAdapter(this, mChosenForm, cr);
					lsv.setAdapter(rowView);
					summaryView = null;
				}
			}
		}
	}

	

}
