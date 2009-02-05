package org.rapidandroid.activity.chart.form;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rapidandroid.activity.chart.ChartBroker;
import org.rapidandroid.activity.chart.JSONGraphData;
import org.rapidandroid.activity.chart.ChartBroker.DateDisplayTypes;
import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidandroid.data.controller.ParsedDataReporter;
import org.rapidsms.java.core.Constants;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.webkit.WebView;

public class FormDataBroker extends ChartBroker {
	public static final int PLOT_ALL_MESSAGES_FOR_FORM = 0;
	public static final int PLOT_NUMERIC_FIELD_VALUE = 1;
	public static final int PLOT_NUMERIC_FIELD_ADDITIVE = 2;
	public static final int PLOT_WORD_HISTOGRAM = 3;
	public static final int PLOT_NUMERIC_FIELD_COUNT_HISTOGRAM = 4;

	private Form mForm;
	private Field fieldToPlot;
	private int mPlotMethod;
	private ProgressDialog mProgress;

	public FormDataBroker(Activity parentActivity, WebView appView, Form form, Date startDate, Date endDate) {
		super(parentActivity,appView,startDate,endDate);
		mForm = form;
		// by default, do all messages for form
		mPlotMethod = PLOT_ALL_MESSAGES_FOR_FORM;

		mVariableStrings= new String[mForm.getFields().length+1];
		mVariableStrings[0] = "Messages over time";
		for (int i = 1; i < mVariableStrings.length; i++) {
			Field f = mForm.getFields()[i-1];
			mVariableStrings[i] = f.getName();
		}		
	}

	public void doLoadGraph() {
		//mProgress = ProgressDialog.show(mAppView.getContext(), "Rendering Graph...", "Please Wait",true,false);
		JSONGraphData allData  = null;
		
		if (fieldToPlot == null) {
			//we're going to do all messages over timereturn;
			allData = loadMessageOverTimeHistogram();
		} else if (fieldToPlot.getFieldType().getItemType().equals("word")) {
			allData = loadHistogramFromField(); 
		} else {
			allData = loadNumericLine(); 
			//data.put(loadNumericLine());
		}
		if (allData != null) {
			mGraphData = allData.getData();
			mGraphOptions = allData.getOptions();
		} 
		Log.d("FormDataBroker",mGraphData.toString());
		Log.d("FormDataBroker",mGraphOptions.toString());		
	}

	
	private JSONGraphData loadNumericLine() {
		JSONObject result = new JSONObject();
		SQLiteDatabase db = rawDB.getReadableDatabase();

		String fieldcol = RapidSmsDBConstants.FormData.COLUMN_PREFIX
				+ fieldToPlot.getName();
		StringBuilder rawQuery = new StringBuilder();
		rawQuery.append("select rapidandroid_message.time, " + fieldcol);
		rawQuery.append(" from ");
		rawQuery.append(RapidSmsDBConstants.FormData.TABLE_PREFIX
				+ mForm.getPrefix());

		rawQuery.append(" join rapidandroid_message on (");
		rawQuery.append(RapidSmsDBConstants.FormData.TABLE_PREFIX
				+ mForm.getPrefix());
		rawQuery.append(".message_id = rapidandroid_message._id");
		rawQuery.append(") ");
		
		if(mStartDate.compareTo(Constants.NULLDATE) != 0 && mEndDate.compareTo(Constants.NULLDATE) != 0) {
			rawQuery.append(" WHERE rapidandroid_message.time > '" + Message.SQLDateFormatter.format(mStartDate) + "' AND rapidandroid_message.time < '" + Message.SQLDateFormatter.format(mEndDate) + "' ");
		}

		rawQuery.append(" order by rapidandroid_message.time ASC");

		// the string value is column 0
		// the magnitude is column 1

		Cursor cr = db.rawQuery(rawQuery.toString(), null);
		int barCount = cr.getCount();

		if (barCount == 0) {
			cr.close();
		} else {
			Date[] xVals = new Date[barCount];
			int[] yVals = new int[barCount];
			cr.moveToFirst();
			int i = 0;
			do {
				try {
					xVals[i] = Message.SQLDateFormatter.parse(cr.getString(0));
					yVals[i] = cr.getInt(1);
				} catch (Exception ex) {

				}
				i++;
			} while (cr.moveToNext());

			// xaxis: { ticks: [0, [Math.PI/2, "\u03c0/2"], [Math.PI, "\u03c0"],
			// [Math.PI * 3/2, "3\u03c0/2"], [Math.PI * 2, "2\u03c0"]]},

			try {
//				result.put("label", fieldToPlot.getName());
//				result.put("data", prepareDateData(xVals, yVals));
//				result.put("label", fieldToPlot.getName());
//				result.put("lines", getShowTrue());
//				result.put("points", getShowTrue());
//				result.put("xaxis", getDateOptions());
				return new JSONGraphData(prepareDateData(xVals, yVals),loadOptionsForDateGraph(xVals, false) );
			} catch (Exception ex) {

			}
			finally {
				if (!cr.isClosed()) {
					cr.close();
				}
			}
			
		}
		// either there was no data or something bad happened
		return new JSONGraphData(getEmptyData(), new JSONObject());	
	}

	
	private JSONArray getEmptyData() {
		JSONArray toReturn = new JSONArray();
		JSONArray innerArray = new JSONArray();
		innerArray.put(0);
		innerArray.put(0);
		toReturn.put(innerArray);
		return toReturn;
	}

	private JSONObject getDateOptions() {
		JSONObject rootxaxis = new JSONObject();

		try {
			rootxaxis.put("mode", "time");
		} catch (Exception ex) {

		}
		return rootxaxis;
	}

	private JSONArray prepareDateHistogramData(DateDisplayTypes displayType, Date[] xvals, int[] yvals, String legend) throws JSONException {
		JSONArray outerArray = new JSONArray();
		JSONArray innerArray = new JSONArray();
		int datalen = xvals.length;
		Date prevVal = null;
		for (int i = 0; i < datalen; i++) {
			Date thisVal = xvals[i];
			if (prevVal != null) {
				// add logic to fill in zeros
				Date nextInSeries = getNextValue(displayType, prevVal);
				while (isBefore(displayType, nextInSeries, thisVal))
				{
					JSONArray elem = new JSONArray();
					elem.put(nextInSeries.getTime());
					elem.put(0);
					innerArray.put(elem);
					nextInSeries = getNextValue(displayType, nextInSeries);
				}
			}
			JSONArray elem = new JSONArray();
			elem.put(xvals[i].getTime());
			elem.put(yvals[i]);
			innerArray.put(elem);
			prevVal = thisVal;
		}
		JSONObject finalObj = new JSONObject();
		finalObj.put("data", innerArray);
		finalObj.put("label", legend);
		outerArray.put(finalObj);
		return outerArray;
	}
	
	private JSONArray prepareDateData(Date[] xvals, int[] yvals) {
		JSONArray outerArray = new JSONArray();
		JSONArray innerArray = new JSONArray();
		int datalen = xvals.length;
		for (int i = 0; i < datalen; i++) {
			JSONArray elem = new JSONArray();
			elem.put(xvals[i].getTime());
			elem.put(yvals[i]);
			innerArray.put(elem);
		}
		outerArray.put(innerArray);
		return outerArray;
	}
	
	private JSONGraphData loadMessageOverTimeHistogram() {
		SQLiteDatabase db = rawDB.getReadableDatabase();
		
		//Date firstDateFromForm = ParsedDataReporter.getOldestMessageDate(rawDB, mForm); 
		Date startDateToUse = mStartDate;
//		if (firstDateFromForm.after(mStartDate)) {
//			// first date in the form is more recent than the start date, so just go with that.
//			startDateToUse = firstDateFromForm;
//		}
		DateDisplayTypes displayType = this.getDisplayType(startDateToUse, mEndDate);
		
		String legend = getLegendString(displayType);
		String selectionArg = getSelectionString(displayType);
		
		StringBuilder rawQuery = new StringBuilder();
		
		rawQuery.append("select time, count(*) from  ");
		rawQuery.append(RapidSmsDBConstants.FormData.TABLE_PREFIX + mForm.getPrefix());
		
		rawQuery.append(" join rapidandroid_message on (");
		rawQuery.append(RapidSmsDBConstants.FormData.TABLE_PREFIX + mForm.getPrefix());
		rawQuery.append(".message_id = rapidandroid_message._id");
		rawQuery.append(") ");
		if(startDateToUse.compareTo(Constants.NULLDATE) != 0 && mEndDate.compareTo(Constants.NULLDATE) != 0) {
			rawQuery.append(" WHERE rapidandroid_message.time > '" + Message.SQLDateFormatter.format(startDateToUse) + "' AND rapidandroid_message.time < '" + Message.SQLDateFormatter.format(mEndDate) + "' ");
		}
		
		rawQuery.append(" group by ").append(selectionArg);		
		rawQuery.append("order by ").append(selectionArg).append(" ASC");
		
	
		// the X date value is column 0
		// the y value magnitude is column 1

		Cursor cr = db.rawQuery(rawQuery.toString(), null);
		int barCount = cr.getCount();

		if (barCount == 0) {
			db.close();
			cr.close();
		} else {
			Date[] xVals = new Date[barCount];
			int[] yVals = new int[barCount];
			cr.moveToFirst();
			int i = 0;
			do {
				xVals[i] = getDate(displayType, cr.getString(0));
				yVals[i] = cr.getInt(1);
				i++;
			} while (cr.moveToNext());

			try {
				//result.put("label", fieldToPlot.getName());
				//result.put("data", prepareData(xVals, yVals));
				//result.put("bars", getShowTrue());
				//result.put("xaxis", getXaxisOptions(xVals));
				// todo 
				return new JSONGraphData(prepareDateHistogramData(displayType, xVals, yVals, legend),loadOptionsForDateGraph(xVals, true) );
				
			} catch (Exception ex) {

			} finally {
				if (!cr.isClosed()) {
					
					cr.close();
				}
				if(db.isOpen()) {
					db.close();
				}
			}
		}
		// either there was no data or something bad happened
		return new JSONGraphData(getEmptyData(), new JSONObject());	
	}
	

	
	
	/**
	 * Should return a two element array - the first element is the data, 
	 * the second are the options
	 * @return
	 */
	private JSONGraphData loadHistogramFromField() {
		//JSONObject result = new JSONObject();
		SQLiteDatabase db = rawDB.getReadableDatabase();

		String fieldcol = RapidSmsDBConstants.FormData.COLUMN_PREFIX
				+ fieldToPlot.getName();
		StringBuilder rawQuery = new StringBuilder();
		rawQuery.append("select " + fieldcol);
		rawQuery.append(", count(*) from ");
		rawQuery.append(RapidSmsDBConstants.FormData.TABLE_PREFIX
				+ mForm.getPrefix());
		
		rawQuery.append(" join rapidandroid_message on (");
		rawQuery.append(RapidSmsDBConstants.FormData.TABLE_PREFIX
				+ mForm.getPrefix());
		rawQuery.append(".message_id = rapidandroid_message._id");
		rawQuery.append(") ");
		
		if(mStartDate.compareTo(Constants.NULLDATE) != 0 && mEndDate.compareTo(Constants.NULLDATE) != 0) {
			rawQuery.append(" WHERE rapidandroid_message.time > '" + Message.SQLDateFormatter.format(mStartDate) + "' AND rapidandroid_message.time < '" + Message.SQLDateFormatter.format(mEndDate) + "' ");
		}

		
		rawQuery.append(" group by " + fieldcol);
		rawQuery.append(" order by " + fieldcol);

		// the string value is column 0
		// the magnitude is column 1

		Cursor cr = db.rawQuery(rawQuery.toString(), null);
		int barCount = cr.getCount();

		if (barCount != 0) {
			String[] xVals = new String[barCount];
			int[] yVals = new int[barCount];
			cr.moveToFirst();
			int i = 0;
			do {
				xVals[i] = cr.getString(0);
				yVals[i] = cr.getInt(1);
				i++;
			} while (cr.moveToNext());

			// xaxis: { ticks: [0, [Math.PI/2, "\u03c0/2"], [Math.PI, "\u03c0"],
			// [Math.PI * 3/2, "3\u03c0/2"], [Math.PI * 2, "2\u03c0"]]},

			try {
				//result.put("label", fieldToPlot.getName());
				//result.put("data", prepareData(xVals, yVals));
				//result.put("bars", getShowTrue());
				//result.put("xaxis", getXaxisOptions(xVals));
				return new JSONGraphData(prepareHistogramData(xVals, yVals),loadOptionsForHistogram(xVals) );
			} catch (Exception ex) {

			} finally {
				if (!cr.isClosed()) {
					cr.close();
				}
				if(db.isOpen()) {
					db.close();
				}
			}
		}
		// either there was no data or something bad happened
		return new JSONGraphData(getEmptyData(), new JSONObject());
	}

	private JSONArray prepareHistogramData(String[] names, int[] counts) throws JSONException {
		// TODO Auto-generated method stub
		JSONArray arr = new JSONArray();
		int datalen = names.length;
		for (int i = 0; i < datalen; i++) {
			
			JSONObject elem = new JSONObject();
			// values will just be an array of length 1 with a single value
			JSONArray values = new JSONArray();
			JSONArray value = new JSONArray();
			value.put(i);
			value.put(counts[i]);
			values.put(value);
			elem.put("data", values);
			elem.put("bars", getShowTrue());
			elem.put("label", names[i]);
			arr.put(elem);
		}
		return arr;
	}
	
	private JSONObject loadOptionsForDateGraph(Date[] vals, boolean displayLegend) throws JSONException {

		JSONObject toReturn = new JSONObject();
		//bars: { show: true }, points: { show: false }, xaxis: { mode: "time", timeformat:"%y/%m/%d" }
		toReturn.put("bars", getShowFalse());
		toReturn.put("lines", getShowTrue());
		toReturn.put("points", getShowFalse());
		toReturn.put("xaxis", getXaxisOptionsForDate());
		if (displayLegend) {
			toReturn.put("legend", getShowTrue());
		} 
		return toReturn;
	}
	
	private JSONObject getXaxisOptionsForDate() throws JSONException {
		JSONObject toReturn = new JSONObject();
		toReturn.put("mode", "time");
		toReturn.put("timeformat", "%m/%d/%y");
		return toReturn;
	}

	private JSONObject loadOptionsForHistogram(String[] labels) throws JSONException {
		
		JSONObject toReturn = new JSONObject();
		toReturn.put("xaxis", this.getXaxisOptions(labels));
		return toReturn;
	}

	// puts the yvalues into the json array for the given x values (defined by
	// the array indices)
	// so output format is [[x0,y0],[x1,y1]...etc]
	// in reality is [[0,values[0]],[1,values[1], etc]
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

	private JSONObject getXaxisOptions(String[] tickvalues) {
		JSONObject rootxaxis = new JSONObject();
		JSONArray arr = new JSONArray();
		int ticklen = tickvalues.length;

		for (int i = 0; i < ticklen; i++) {
			JSONArray elem = new JSONArray();
			elem.put(i);
			elem.put(tickvalues[i]);
			arr.put(elem);
		}

		try {
			rootxaxis.put("ticks", arr);
			rootxaxis.put("tickFormatter", "string");
		} catch (Exception ex) {

		}
		return rootxaxis;
	}

	private JSONArray getRandomData() {
		Random rand = new Random();
		JSONArray arr = new JSONArray();
		int priorval = rand.nextInt(100);
		for (int i = 0; i < 100; i++) {
			JSONArray elem = new JSONArray();
			elem.put(i);
			if (rand.nextBoolean()) {
				priorval += rand.nextInt(10);
			} else {
				priorval -= rand.nextInt(10);
			}
			elem.put(priorval);
			arr.put(elem);
		}
		return arr;
	}

	private JSONObject getLineOptionsJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("show", true);
		} catch (Exception ex) {

		}
		return ret;
	}

	private JSONObject getShowTrue() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("show", true);
		} catch (Exception ex) {

		}
		return ret;
	}

	private JSONObject getShowFalse() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("show", false);
		} catch (Exception ex) {

		}
		return ret;
	}

	public String getGraphTitle() {
		return "my line baby";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.ChartBroker#setVariable(int)
	 */

	public void setVariable(int id) {
		// TODO Auto-generated method stub
		if(id == 0) {
			this.fieldToPlot = null;
		} else {
			this.fieldToPlot = mForm.getFields()[id-1];
		}
		this.mGraphData = null;
		this.mGraphOptions = null;
	}
/* (non-Javadoc)
	 * @see org.rapidandroid.activity.chart.ChartBroker#finishGraph()
	 */
	
	public String getName() {
		return "graph_form";
	}
}
