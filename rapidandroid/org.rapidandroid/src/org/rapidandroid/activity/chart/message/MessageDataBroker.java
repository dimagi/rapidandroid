package org.rapidandroid.activity.chart.message;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rapidandroid.activity.chart.ChartBroker;
import org.rapidandroid.activity.chart.JSONGraphData;
import org.rapidsms.java.core.Constants;
import org.rapidsms.java.core.model.Message;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.WebView;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 29, 2009 Summary:
 */
public class MessageDataBroker extends ChartBroker {
	public MessageDataBroker(Activity activity, WebView appView, Date startDate, Date endDate) {
		super(activity,appView,startDate,endDate);
		mVariableStrings = new String[] { "Trends by day", "Receipt time of day" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.ChartBroker#getGraphTitle()
	 */
	public String getGraphTitle() {
		// TODO Auto-generated method stub
		return "message graphs";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.ChartBroker#loadGraph()
	 */

	public void doLoadGraph() {
		//mParentActivity.showDialog(160);
		//Progress = ProgressDialog.show(mAppView.getContext(), "Rendering Graph...", "Please Wait",true,false);		
		//isLoading..mToggleThinkerHandler.post(mToggleThinker);
		JSONGraphData allData  = null;
		if (mChosenVariable == 0) {
			// this is a count of messages per day
			// select date(time), count(*) from rapidandroid_message group by
			// date(time)
			allData = loadMessageTrends();
		} else if (mChosenVariable == 1) {
			mGraphData.put(chartMessagesPerHour());
		}
		if (allData != null) {
			mGraphData = allData.getData();
			mGraphOptions = allData.getOptions();
		} else {
			mGraphData = this.getEmptyData();
			mGraphOptions = new JSONObject();
		}

	}

	private JSONGraphData loadMessageTrends() {
		SQLiteDatabase db = rawDB.getReadableDatabase();
		
		Date startDateToUse = mStartDate;
//		if (firstDateFromForm.after(mStartDate)) {
//			// first date in the form is more recent than the start date, so just go with that.
//			startDateToUse = firstDateFromForm;
//		}
		DateDisplayTypes displayType = this.getDisplayType(startDateToUse, mEndDate);
		
		String selectionArg = getSelectionString(displayType);
		
		StringBuilder rawQuery = new StringBuilder();
		rawQuery.append("select time, count(*) from rapidandroid_message ");
		if(startDateToUse.compareTo(Constants.NULLDATE) != 0 && mEndDate.compareTo(Constants.NULLDATE) != 0) {
			rawQuery.append(" WHERE rapidandroid_message.time > '" + Message.SQLDateFormatter.format(startDateToUse) + "' AND rapidandroid_message.time < '" + Message.SQLDateFormatter.format(mEndDate) + "' ");
		}
		rawQuery.append(" group by ").append(selectionArg);		
		rawQuery.append(" order by ").append(selectionArg).append(" ASC");
		
		
		// the X date value is column 0
		// the y value magnitude is column 1

		Cursor cr = db.rawQuery(rawQuery.toString(), null);
		return this.getDateQuery(displayType, cr, db);
	}

	private JSONObject chartMessagesPerHour() {
		JSONObject result = new JSONObject();
		SQLiteDatabase db = rawDB.getReadableDatabase();

		String rawQuery = "select strftime('%H',time), count(*) from rapidandroid_message group by strftime('%H',time) order by strftime('%H',time)";

		// the string value is column 0
		// the magnitude is column 1

		Cursor cr = db.rawQuery(rawQuery, null);
		int barCount = cr.getCount();

		if (barCount == 0) {
			return result;
		} else {
			String[] xVals = new String[barCount];
			int[] yVals = new int[barCount];
			cr.moveToFirst();
			int i = 0;
			do {
				xVals[i] = cr.getString(0);
				yVals[i] = cr.getInt(1);
				i++;
			} while (cr.moveToNext());

			try {
				result.put("label", "Messages");
				result.put("data", prepareData(yVals));
				result.put("bars", getShowTrue());

			} catch (Exception ex) {

			}
			cr.close();	
			return result;
		}
	}

	private JSONArray prepareData(int[] values) {
		JSONArray arr = new JSONArray();
		int datalen = values.length;
		for (int i = 0; i < datalen; i++) {
			JSONArray elem = new JSONArray();
			elem.put(i);
			elem.put(values[i]);
			arr.put(elem);
		}
		return arr;
	}

	private JSONObject getDateOptions() {
		JSONObject rootxaxis = new JSONObject();

		try {
			rootxaxis.put("mode", "time");
		} catch (Exception ex) {

		}
		return rootxaxis;
	}

	
	/* (non-Javadoc)
	 * @see org.rapidandroid.activity.chart.ChartBroker#finishGraph()
	 */
	public void finishGraph() {
		mToggleThinkerHandler.post(mToggleThinker);		
		mTitleHandler.post(mUpdateActivityTitle);		
	}
	
	public String getName() {
		return "graph_msg";
	}

}
