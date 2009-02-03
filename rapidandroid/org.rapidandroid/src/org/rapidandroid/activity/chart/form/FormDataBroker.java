package org.rapidandroid.activity.chart.form;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rapidandroid.activity.chart.IChartBroker;
import org.rapidandroid.data.RapidSmsDBConstants;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.Constants;
import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;
import org.rapidsms.java.core.model.Message;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.WebView;
import android.widget.Toast;

public class FormDataBroker implements IChartBroker {
	private WebView mAppView;

	SmsDbHelper rawDB;

	public static final int PLOT_ALL_MESSAGES_FOR_FORM = 0;
	public static final int PLOT_NUMERIC_FIELD_VALUE = 1;
	public static final int PLOT_NUMERIC_FIELD_ADDITIVE = 2;
	public static final int PLOT_WORD_HISTOGRAM = 3;
	public static final int PLOT_NUMERIC_FIELD_COUNT_HISTOGRAM = 4;

	private Form mForm;
	private Field fieldToPlot;
	private int mPlotMethod;

	private Date mStartDate = Constants.NULLDATE;
	private Date mEndDate = Constants.NULLDATE;
	
	private String[] variables;

	private ProgressDialog mProgress;

	public FormDataBroker(WebView appView, Form form, Date startDate, Date endDate) {
		this.mAppView = appView;
		mForm = form;
		// by default, do all messages for form
		mPlotMethod = PLOT_ALL_MESSAGES_FOR_FORM;
		// mAppView.getGlobalVisibleRect(r)
		this.rawDB = new SmsDbHelper(appView.getContext());
		Context c = appView.getContext();		

		this.variables = new String[mForm.getFields().length+1];
		variables[0] = "Messages over time";
		for (int i = 1; i < variables.length; i++) {
			Field f = mForm.getFields()[i-1];
			variables[i] = f.getName();
		}
		
		mStartDate = startDate;
		mEndDate = endDate;
		Toast.makeText(appView.getContext(), "To see chart, load a variable with the menus below.",Toast.LENGTH_LONG );
	}

	public void loadGraph() {
		mProgress = ProgressDialog.show(mAppView.getContext(), "Rendering Graph...", "Please Wait",true,false);
		int width = mAppView.getWidth();
		int height = 0;
		if (width == 480) {
			height = 320;
		} else if (width == 320) {
			height = 480;
		}
		height = height - 50;

		JSONArray arr = new JSONArray();

		if (fieldToPlot == null) {
			//we're going to do all messages over timereturn;
			arr.put(loadMessageOverTimeHistogram());
		} else if (fieldToPlot.getFieldType().getItemType().equals("word")) {
			arr.put(loadHistogramFromField());
		} else {
			arr.put(loadNumericLine());
		}

		mAppView.loadUrl("javascript:SetGraph(\"" + width + "px\", \"" + height
				+ "px\")");
		mAppView.loadUrl("javascript:GotGraph(" + arr.toString() + ")");
	}

	private JSONObject loadNumericLine() {
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
			return result;
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
				result.put("label", fieldToPlot.getName());
				result.put("data", prepareDateData(xVals, yVals));
				result.put("label", fieldToPlot.getName());
				result.put("lines", getShowTrue());
				result.put("points", getShowTrue());
				result.put("xaxis", getDateOptions());
			} catch (Exception ex) {

			}
			cr.close();
			return result;
		}

	}

	private JSONObject getDateOptions() {
		JSONObject rootxaxis = new JSONObject();

		try {
			rootxaxis.put("mode", "time");
		} catch (Exception ex) {

		}
		return rootxaxis;
	}

	private JSONArray prepareDateData(Date[] xvals, int[] yvals) {
		JSONArray arr = new JSONArray();
		int datalen = xvals.length;
		for (int i = 0; i < datalen; i++) {
			JSONArray elem = new JSONArray();
			elem.put(xvals[i].getTime());
			elem.put(yvals[i]);
			arr.put(elem);
		}
		return arr;
	}
	
	private JSONObject loadMessageOverTimeHistogram() {
		JSONObject result = new JSONObject();
		SQLiteDatabase db = rawDB.getReadableDatabase();
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(mStartDate);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(mEndDate);
		String legend = "";
				
		String selectionArg = "";
		if (endCal.get(Calendar.YEAR) == startCal.get(Calendar.YEAR) &&
				   endCal.get(Calendar.MONTH) == startCal.get(Calendar.MONTH) &&
				   endCal.get(Calendar.DATE) - startCal.get(Calendar.DATE) < 3) {
			//within 3 days, we do it by hour. with day shading
			selectionArg = "  strftime('%H',time) ";
			legend = "Hourly count";
			
		} 
		
		else if (endCal.get(Calendar.YEAR) == startCal.get(Calendar.YEAR) &&
				   endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH) < 3) {
			//within 3 months, we break it down by day with week & month shading?
			selectionArg = " strftime('%d', time) ";
			legend = "Daily count";
			
		}
		else if (endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR) < 2) {
			//within 2 years, we break it down by week with month shading
			selectionArg = " strftime('%W', time) ";
			legend = "Weekly count";
			
		}
		else if (endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR) < 4) {
			// 2-4 years break it down by month with year shading
			selectionArg = " strftime('%m',time) ";
			legend = "Monthly count";
			
		} else if(endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR) >= 4) {
			//we need to break it down by year. with year shading
			selectionArg = " strftime('%Y',time) ";
			legend = "Annual count";
		} 
		StringBuilder rawQuery = new StringBuilder();
		
		rawQuery.append("select ").append(selectionArg).append(", count(*) from  ");
		rawQuery.append(RapidSmsDBConstants.FormData.TABLE_PREFIX + mForm.getPrefix());
		
		rawQuery.append(" join rapidandroid_message on (");
		rawQuery.append(RapidSmsDBConstants.FormData.TABLE_PREFIX + mForm.getPrefix());
		rawQuery.append(".message_id = rapidandroid_message._id");
		rawQuery.append(") ");
		rawQuery.append(" group by ").append(selectionArg);		
		rawQuery.append("order by ").append(selectionArg).append(" ASC");
		
	
		// the X date value is column 0
		// the y value magnitude is column 1

		Cursor cr = db.rawQuery(rawQuery.toString(), null);
		int barCount = cr.getCount();

		if (barCount == 0) {
			cr.close();
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
				result.put("label", legend);
				result.put("data", prepareData(yVals));
				result.put("bars", getShowTrue());
				result.put("points", getShowFalse());
				result.put("xaxis", getXaxisOptions(xVals));

			} catch (Exception ex) {

			}
			cr.close();
			return result;
		}

	}
	

	private JSONObject loadHistogramFromField() {
		JSONObject result = new JSONObject();
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

			// xaxis: { ticks: [0, [Math.PI/2, "\u03c0/2"], [Math.PI, "\u03c0"],
			// [Math.PI * 3/2, "3\u03c0/2"], [Math.PI * 2, "2\u03c0"]]},

			try {
				result.put("label", fieldToPlot.getName());
				result.put("data", prepareData(yVals));
				result.put("bars", getShowTrue());
				result.put("xaxis", getXaxisOptions(xVals));

			} catch (Exception ex) {

			}
			cr.close();
			return result;
		}

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

	// THIS ALSO WORKS!!
	public String getGraphTitle(String arg, int intarg) {
		return "my line baby: " + arg.length() * intarg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.IChartBroker#getVariables()
	 */

	public String[] getVariables() {
		// TODO Auto-generated method stub
		return variables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.IChartBroker#setVariable(int)
	 */

	public void setVariable(int id) {
		// TODO Auto-generated method stub
		if(id == 0) {
			this.fieldToPlot = null;
		} else {
			this.fieldToPlot = mForm.getFields()[id-1];
		}
	}

	public void setRange(Date startTime, Date endTime) {
		mStartDate = startTime;
		mEndDate= endTime;
	}

	/* (non-Javadoc)
	 * @see org.rapidandroid.activity.chart.IChartBroker#finishGraph()
	 */
	public void finishGraph() {
		if(mProgress!= null) {
			mProgress.dismiss();
			mProgress= null;
		}
		
	}
}
