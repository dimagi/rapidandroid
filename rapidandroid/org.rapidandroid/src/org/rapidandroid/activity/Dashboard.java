/**
 * 
 */
package org.rapidandroid.activity;

import org.rapidandroid.ActivityConstants;
import org.rapidandroid.R;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidandroid.view.SingleRowHeaderView;
import org.rapidandroid.view.adapter.FormDataCursorAdapter;
import org.rapidandroid.view.adapter.MessageCursorAdapter;
import org.rapidandroid.view.adapter.InefficientParsedMessageViewAdapter;
import org.rapidandroid.view.adapter.SummaryCursorAdapter;
import org.rapidsms.java.core.model.Form;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
		
	private SingleRowHeaderView headerView;
	private SummaryCursorAdapter summaryView; 
	private FormDataCursorAdapter rowView;
	private MessageCursorAdapter messageCursorAdapter;
	
	private ProgressDialog mLoadingDialog;
	
	
	private Form mChosenForm = null;
	
		
	private boolean mShowMonitors = false;
	private boolean mShowAllMessages = false;

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
	
	private static final int DIALOG_LOADING = 17;
	
	private int formViewMode = 0;
	
	
	private Form[] mAllForms;	
	
	boolean formChanged = true; 
	Cursor mListviewCursor = null; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.dashboard);

		if (savedInstanceState != null) {
			String from = savedInstanceState.getString("from");
			String body = savedInstanceState.getString("body");
			//dialogMessage = "SMS :: " + from + " : " + body;
			//showDialog(160);
		}
		
		mLoadingDialog = new ProgressDialog(this,ProgressDialog.STYLE_HORIZONTAL);
		mLoadingDialog.setMessage("Loading data...");
		mLoadingDialog.setTitle("Please wait");
		mLoadingDialog.setIndeterminate(true);
		mLoadingDialog.setCancelable(false);			

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
						formChanged = true;
						loadListViewWithFormData();
					}
				});

		// add some events to the listview
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);

		
		lsv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		// bind a context menu
		lsv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				if (mChosenForm != null) {
					menu.add(0, CONTEXT_ITEM_SUMMARY_VIEW, 0, "Summary View");
					menu.add(0, CONTEXT_ITEM_TABLE_VIEW, 0, "Table View");
				} else {
					menu.clear();
				}
			}
		});		
		
		
		lsv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long row) {
				if(adapter.getAdapter().getClass().equals(SummaryCursorAdapter.class) ) {
					((SummaryCursorAdapter) adapter.getAdapter()).toggle(position);
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
//			dialogMessage = "Activity Done";
//			showDialog(12);
			break;
		case ACTIVITY_CHARTS:
//			dialogMessage = "Activity Done";
//			showDialog(13);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// add images:
		// http://developerlife.com/tutorials/?p=304
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CREATE_ID, 0, R.string.dashboard_menu_create).setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, MENU_FORM_REVIEW_ID, 0, R.string.dashboard_menu_edit).setIcon(android.R.drawable.ic_menu_agenda);
		menu.add(0, MENU_CHARTS_ID, 0, R.string.dashboard_menu_view).setIcon(android.R.drawable.ic_menu_sort_by_size);
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
			startActivityChart();
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

		return true;
	}

	
	
	@Override
	// http://www.anddev.org/tinytutcontextmenu_for_listview-t4019.html
	// UGH, things changed from .9 to 1.0
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXT_ITEM_SUMMARY_VIEW:
			formViewMode = LISTVIEW_MODE_SUMMARY_VIEW;			
			break;
		case CONTEXT_ITEM_TABLE_VIEW:			
			formViewMode = LISTVIEW_MODE_TABLE_VIEW;			
			break;
		default:
			return super.onContextItemSelected(item);
		}
		this.formChanged = false;
		beginListViewReload();
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
	
	private void startActivityChart() {
		Intent i = new Intent(this, ChartData.class);
		
		if(mChosenForm != null && !mShowAllMessages && !mShowMonitors) {
			i.putExtra(ActivityConstants.CHART_FORM, mChosenForm.getFormId());			
		} else if(mShowAllMessages && !mShowMonitors) {
			//show the messages
			i.putExtra(ActivityConstants.CHART_MESSAGES, true);
		} else if (mShowMonitors && !mShowAllMessages) {
			//show all the monitrors
		}		
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
			this.mShowAllMessages = true;
			this.mShowMonitors = false;
			formChanged = true;
			beginListViewReload();
			//loadListViewWithRawMessages();
			

		} else if (position == mAllForms.length + 1) {
			// then it's show all monitors			
			mChosenForm = null;
			this.mShowAllMessages = false;
			this.mShowMonitors = true;
			formChanged = true;
			beginListViewReload();
			//loadListViewsWithMonitors();
			
		} else {
			this.mShowAllMessages = false;
			this.mShowMonitors = false;
			mChosenForm = mAllForms[position];
			formChanged = true;
			beginListViewReload();
			//loadListViewWithFormData(true);
		}		
	}
		
	
	
    
    private void finishListViewReload() {
    	ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);		
		
    	if(mChosenForm != null && !mShowAllMessages && !mShowMonitors) {
    		loadListViewWithFormData();
    	}
    	else if(mShowAllMessages && mChosenForm == null && !mShowMonitors) {
			this.messageCursorAdapter = new MessageCursorAdapter(this, mListviewCursor);
			lsv.setAdapter(messageCursorAdapter);
    	}
    	else if(mShowMonitors && !mShowAllMessages && mChosenForm==null) {    		
    		lsv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, new String[] {"todo"}));
    		
    	}    	
    }
    
    private void beginListViewReload() {
    	//mLoadingDialog = ProgressDialog.show(this,"Loading data", "Please wait");
    	mLoadingDialog.show();
    	resetListAdapters();
    	final Handler mDashboardHandler = new Handler();
    	final Runnable mUpdateResults = new Runnable() {
            public void run() {
            	finishListViewReload();
            	mLoadingDialog.dismiss();    	
            }
        };
    	
    	Thread t = new Thread() {
            public void run() {            	
            	fillCursorInBackground();            	            	
            	mDashboardHandler.post(mUpdateResults);
            }
        };
        t.start();
    }
    
    private void fillCursorInBackground() {
    	if(mListviewCursor == null) {
	    	if(mChosenForm != null && !mShowAllMessages && !mShowMonitors) {
	    		mListviewCursor = getContentResolver().query(Uri.parse(RapidSmsDBConstants.FormData.CONTENT_URI_PREFIX + mChosenForm.getFormId()), null,null,null,"message_id desc");	
	    	} else if(mShowAllMessages && mChosenForm == null && !mShowMonitors) {
	    		mListviewCursor = getContentResolver().query(RapidSmsDBConstants.Message.CONTENT_URI, null, null, null, "time DESC");
	    	} else if(mShowMonitors && !mShowAllMessages && mChosenForm==null) {
	    		//do something
	    	}    	
    	}
    	
    }
    
	// this is a call to the DB to update the ListView with the messages for a
	// selected form
	private void loadListViewWithFormData() {
				
		ListView lsv = (ListView) findViewById(R.id.lsv_dashboardmessages);
		if (mChosenForm == null) {
			lsv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
													new String[] { "Select an item" }));
		} else {
			
			if(mListviewCursor.getCount() == 0) {
				lsv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
						new String[] { "No data" }));
				return;
			}
				
				/*
				 * if we want to get super fancy, we can do a join to make it all accessible in one cursor instead of having to requery
				 * select formdata_bednets.*, rapidandroid_message.message,rapidandroid_message.time from formdata_bednets
join rapidandroid_message on (formdata_bednets.message_id = rapidandroid_message._id)
				 */			
			
//				if (headerView == null) {
//					this.headerView = new SingleRowHeaderView(this, mChosenForm);
//					lsv.addHeaderView(headerView);
//				
				if(this.formViewMode == Dashboard.LISTVIEW_MODE_SUMMARY_VIEW) {										
					//headerView.setVisibility(View.INVISIBLE);
					this.summaryView = new SummaryCursorAdapter(this, mListviewCursor, mChosenForm);						
					lsv.setAdapter(summaryView);
				} else if(this.formViewMode == Dashboard.LISTVIEW_MODE_TABLE_VIEW) {
					rowView = new FormDataCursorAdapter(this, mChosenForm, mListviewCursor);
					lsv.setAdapter(rowView);					
				}
			}
			
		}

	/**
	 * @param changedforms
	 */
	private void resetListAdapters() {
		if(rowView != null) {
			rowView = null;
		}
		if(summaryView != null) {
			summaryView = null;
		}
		if(messageCursorAdapter != null) {			
			messageCursorAdapter = null;
		}
		//monitorCursorAdapter
		
		if(formChanged) {
			//need to reset the cursor
			if(mListviewCursor != null) {
				mListviewCursor.close();
				mListviewCursor = null;
			}
			formChanged = false;
		}		
	}
	

	

}
