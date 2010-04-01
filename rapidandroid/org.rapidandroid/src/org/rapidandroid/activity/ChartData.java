/*
 * Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package org.rapidandroid.activity;

import java.util.Date;

import org.rapidandroid.R;
import org.rapidandroid.activity.chart.ChartBroker;
import org.rapidandroid.activity.chart.form.FormDataBroker;
import org.rapidandroid.activity.chart.message.MessageDataBroker;
import org.rapidandroid.content.translation.ModelTranslator;
import org.rapidandroid.data.controller.MessageDataReporter;
import org.rapidandroid.data.controller.ParsedDataReporter;
import org.rapidsms.java.core.Constants;
import org.rapidsms.java.core.model.Form;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 28, 2009 Summary:
 */
public class ChartData extends Activity {
	public class CallParams {
		public static final String CHART_FORM = "graph_form";
		public static final String CHART_MESSAGES = "graph_msg";
		public static final String CHART_MONITORS = "graph_monitor";

		public static final String START_DATE = "startdate";
		public static final String END_DATE = "enddate";
	}

	private static final String STATE_CHART_FOR = "charttype";
	private static final String STATE_START_DATE = "startdate";
	private static final String STATE_END_DATE = "enddate";
	private static final String STATE_SELECTED_VARIABLE = "variable";
	private static final String STATE_SELECTED_FORM = "form";
	private static final String STATE_GRAPH_DATA = "graphdata";
	private static final String STATE_GRAPH_OPTION = "graphoption";

	private static final int MENU_DONE = Menu.FIRST;
	private static final int MENU_CHANGE_VARIABLE = Menu.FIRST + 1;
	private static final int MENU_CHANGE_DATERANGE = Menu.FIRST + 2;

	private static final int ACTIVITY_DATERANGE = 7;
	private static final int THINKING_DIALOG = 160;
	private static final int NO_DATA_DIALOG = 170;

	private Date mStartDate;
	private Date mEndDate;
	private int mVariable;

	private Form mForm;
	ChartBroker mBroker;
	private WebView mWebView;	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// getWindow().setTitle("Viewing Some data"); //this doesn't seem to
		// work just yet. may need to use the
		// com.example.android.apis.app.CustomTitle example and the title
		// layout. ugh.

		setContentView(org.rapidandroid.R.layout.data_chart);
		mWebView = (WebView) findViewById(org.rapidandroid.R.id.wv1);
		mWebView.getSettings().setJavaScriptEnabled(true);		

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mStartDate = Constants.NULLDATE;
			mEndDate = Constants.NULLDATE;

			if (extras.containsKey(CallParams.START_DATE)) {
				mStartDate = new Date(extras.getLong(CallParams.START_DATE));
			}
			if (extras.containsKey(CallParams.END_DATE)) {
				mEndDate = new Date(extras.getLong(CallParams.END_DATE));
			}
			if (extras.containsKey(CallParams.CHART_FORM)) {
				mForm = ModelTranslator.getFormById(extras.getInt(CallParams.CHART_FORM));
				mBroker = new FormDataBroker(this, mWebView, mForm, mStartDate, mEndDate);
			} else if (extras.containsKey(CallParams.CHART_MESSAGES)) {
				mBroker = new MessageDataBroker(this, mWebView, mStartDate, mEndDate);
			} else if (extras.containsKey(CallParams.CHART_MONITORS)) {

			}
			if (savedInstanceState == null) {
				mBroker.bindChartToHTML();
			} 
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState.containsKey(STATE_SELECTED_VARIABLE) && savedInstanceState.containsKey(STATE_START_DATE)
				&& savedInstanceState.containsKey(STATE_END_DATE) && savedInstanceState.containsKey(STATE_CHART_FOR)
				&& savedInstanceState.containsKey(STATE_SELECTED_VARIABLE)
				&& savedInstanceState.containsKey(STATE_GRAPH_OPTION)
				&& savedInstanceState.containsKey(STATE_GRAPH_DATA)) {
			mVariable = savedInstanceState.getInt(STATE_SELECTED_VARIABLE);
			mStartDate = new Date(savedInstanceState.getLong(STATE_START_DATE));
			mEndDate = new Date(savedInstanceState.getLong(STATE_END_DATE));
			String chartfor = savedInstanceState.getString(STATE_CHART_FOR);

			if (chartfor.equals(CallParams.CHART_FORM)) {
				mForm = ModelTranslator.getFormById(savedInstanceState.getInt(STATE_SELECTED_FORM));
				mBroker = new FormDataBroker(this, mWebView, mForm, mStartDate, mEndDate);
			} else if (chartfor.equals(CallParams.CHART_MESSAGES)) {
				mBroker = new MessageDataBroker(this, mWebView, mStartDate, mEndDate);
			} else if (chartfor.equals(CallParams.CHART_MONITORS)) {

			}

			mBroker.setVariable(mVariable);

			mBroker.setGraphData(savedInstanceState.getString(STATE_GRAPH_DATA));
			mBroker.setGraphOptions(savedInstanceState.getString(STATE_GRAPH_OPTION));
		}
		mBroker.bindChartToHTML();		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putLong(STATE_START_DATE, mStartDate.getTime());
		outState.putLong(STATE_END_DATE, mEndDate.getTime());
		outState.putInt(STATE_SELECTED_VARIABLE, mVariable);
		outState.putString(STATE_CHART_FOR, mBroker.getName());

		outState.putString(STATE_GRAPH_DATA, mBroker.getGraphData());
		outState.putString(STATE_GRAPH_OPTION, mBroker.getGraphOptions());
		if (mForm != null) {
			outState.putInt(STATE_SELECTED_FORM, mForm.getFormId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stubz
		super.onCreateDialog(id);

		switch (id) {
			case MENU_CHANGE_VARIABLE:
				return new AlertDialog.Builder(ChartData.this)
																.setTitle("Choose Field")
																.setSingleChoiceItems(
																						mBroker.getVariables(),
																						0,
																						new DialogInterface.OnClickListener() {
																							public void onClick(
																									DialogInterface dialog,
																									int whichButton) {
																								mVariable = whichButton;
																								mBroker
																										.setVariable(whichButton);
																								// dialog.dismiss();
																							}
																						})
																.setPositiveButton(
																					"Ok",
																					new DialogInterface.OnClickListener() {
																						public void onClick(
																								DialogInterface dialog,
																								int whichButton) {

																							mBroker.jsLoadGraph();
																						}
																					})
																.setNegativeButton(
																					"Cancel",
																					new DialogInterface.OnClickListener() {
																						public void onClick(
																								DialogInterface dialog,
																								int whichButton) {

																						}
																					}).create();
			case THINKING_DIALOG:
				ProgressDialog loadingDialog = new ProgressDialog(this);
				loadingDialog.setTitle("Please wait");
				loadingDialog.setMessage("Drawing graph...");
				loadingDialog.setIndeterminate(true);
				loadingDialog.setCancelable(false);
				return loadingDialog;
			case NO_DATA_DIALOG:
				return new AlertDialog.Builder(ChartData.this)
																.setTitle("No Data")
																.setMessage(
																			"Sorry, there was no data for the selected variable and date range.")
																.setPositiveButton(
																					"Ok",
																					new DialogInterface.OnClickListener() {
																						public void onClick(
																								DialogInterface dialog,
																								int whichButton) {

																						}
																					}).create();
			default:
				return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_DONE, 0, R.string.chart_menu_done).setIcon(android.R.drawable.ic_menu_revert);
		menu.add(0, MENU_CHANGE_VARIABLE, 0, R.string.chart_menu_change_variable)
			.setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(0, MENU_CHANGE_DATERANGE, 0, R.string.chart_menu_change_parameters)
			.setIcon(android.R.drawable.ic_menu_recent_history);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case MENU_DONE:
				finish();
				return true;
			case MENU_CHANGE_VARIABLE:
				showDialog(MENU_CHANGE_VARIABLE);
				return true;
			case MENU_CHANGE_DATERANGE:
				startDateRangeActivity();
				return true;
		}
		return true;
	}

	private void startDateRangeActivity() {
		Intent i = new Intent(this, DateRange.class);
		Date endDate = new Date();
		if (mForm != null) {
			endDate = ParsedDataReporter.getOldestMessageDate(this, mForm);
		} else {
			endDate = MessageDataReporter.getOldestMessageDate(this);
		}

		i.putExtra(DateRange.CallParams.ACTIVITY_ARG_STARTDATE, endDate.getTime());
		startActivityForResult(i, ACTIVITY_DATERANGE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		Bundle extras = null;
		if (intent != null) {
			extras = intent.getExtras(); // right now this is a case where we
			// don't do much activity back and
			// forth
		}

		switch (requestCode) {
			case ACTIVITY_DATERANGE:
				if (extras != null) {
					mStartDate.setTime(extras.getLong(DateRange.ResultParams.RESULT_START_DATE));
					mEndDate.setTime(extras.getLong(DateRange.ResultParams.RESULT_END_DATE));
					mBroker.setRange(mStartDate, mEndDate);
					mBroker.jsLoadGraph();
				}
				break;

		}
	}
}
