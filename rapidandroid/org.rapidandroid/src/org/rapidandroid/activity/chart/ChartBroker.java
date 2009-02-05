package org.rapidandroid.activity.chart;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.Constants;
import org.rapidsms.java.core.model.Message;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ViewSwitcher;


/**
 * Interface for simple chart display.
 * 
 * The implementers of this interface will need access to database methods to prepare data and output to the graphing system. * 
 * This class is the exposed Java object that the WebView will need to call, specifically the method loadGraph().
 * 
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 29, 2009
 * 
 */
public abstract class ChartBroker {

	private static final String CHART_FILE = "file:///android_asset/flot/html/basechart.html";
	private static final String JAVASCRIPT_PROPERTYNAME = "graphdata";
	private static final String EMPTY_FILE = "file:///android_asset/flot/html/empty.html";
	
	/**
	 * Enumeration for display types (date) for level of bucketization
	 * @author Cory Zue
	 *
	 */
	public enum DateDisplayTypes {
		Hourly,
		Daily,
		Weekly,
		Monthly,
		Yearly
	}
	
	protected boolean isLoading;
	protected Date mStartDate = Constants.NULLDATE;
	protected Date mEndDate = Constants.NULLDATE;
	
	protected SmsDbHelper rawDB;
	protected WebView mAppView;
	
	protected String[] mVariableStrings;
	protected int mChosenVariable = 0;
	protected Activity mParentActivity;
	protected boolean isShowing = false;
	
	protected JSONArray mGraphData;
	protected JSONObject mGraphOptions;
	
	protected final Handler mTitleHandler = new Handler();
	protected final Runnable mUpdateActivityTitle = new Runnable() {
		public void run() {
			mParentActivity.setTitle(mVariableStrings[mChosenVariable]);
		}
	};
	
	protected final Handler mToggleThinkerHandler = new Handler();
	protected final Runnable mToggleThinker = new Runnable() {
		public void run() {
			if(isShowing) {
				mParentActivity.dismissDialog(160);
				isShowing = false;
			} else {
				mParentActivity.showDialog(160);
				isShowing = true;
			}			
		}
	};
	
	protected boolean mGotData;
	private boolean mChartPageLoaded;
	private boolean mAlreadyLoading;
	
	protected ChartBroker(Activity activity, WebView appView, Date startDate, Date endDate) {
		mParentActivity = activity;
		mAppView = appView;
		rawDB = new SmsDbHelper(appView.getContext());
		//mVariableStrings = new String[] { "Trends by day", "Receipt time of day" };
		mStartDate = startDate;
		mEndDate= endDate;
	}
	
	public String getGraphData() {
		if(mGraphData != null) {
			return mGraphData.toString();
		} else {
			return null;
		}
	}
	public synchronized void  setGraphData(String jsonarr) {
		
		try {
			this.mGraphData = new JSONArray(jsonarr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
public synchronized void setGraphOptions(String jsonobj) {
		
		try {
			this.mGraphOptions = new JSONObject(jsonobj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized String getGraphOptions() {
		if(mGraphOptions != null) {
			return mGraphOptions.toString();
		} else {
			return null;
		}
	}
	
	protected abstract void doLoadGraph();
	
	/**
	 * This is the primary method that the JavaScript in our HTML form will need access to in order to display graph data. 
	 */
	public synchronized final void loadGraph() {
		if (!mAlreadyLoading) {
			mToggleThinkerHandler.post(mToggleThinker);
			if(mGraphData == null && mGraphOptions == null) {
				doLoadGraph();
			}
			loadGraphFinish();	
		} 
		mAlreadyLoading = false;
	}
	
	protected void loadGraphFinish(){
		if (!mGotData) {
			mAppView.loadUrl(EMPTY_FILE);
			mChartPageLoaded = false;
			finishGraph();
			return;
		} 
		else if (!mChartPageLoaded) {
			mAlreadyLoading = true;
			reloadChartPage();
		}
		int width = mAppView.getWidth();
		int height = 0;
		if (width == 480) {
			height = 320;
		} else if (width == 320) {
			height = 480;
		}
		height = height - 50;
		mAppView.loadUrl("javascript:SetGraph(\"" + width + "px\", \"" + height	+ "px\")");
		mAppView.loadUrl("javascript:GotGraph(" + mGraphData.toString() + "," + mGraphOptions.toString() + ")");
	}

	private void reloadChartPage() {
		// don't add the js interface
		mAppView.loadUrl(CHART_FILE);
		mChartPageLoaded = true;
	}

	public void loadChartPage() {
		mAppView.addJavascriptInterface(this, JAVASCRIPT_PROPERTYNAME);		
		mAppView.loadUrl(CHART_FILE);
		mChartPageLoaded = true;
	}

	/**
	 * Gets the display type for this, based on the start and end dates
	 * @return
	 */
	protected DateDisplayTypes getDisplayType(Date startDate, Date endDate)
	{
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		Calendar tempCal = Calendar.getInstance();
		tempCal.setTime(startDate);
		tempCal.add(Calendar.DATE, 3);
		if (endCal.before(tempCal)) {
			//within 3 days, we do it by hour. with day shading
			return DateDisplayTypes.Hourly;
		} 
		
		tempCal.setTime(startDate);
		tempCal.add(Calendar.MONTH, 3);
		
		if (endCal.before(tempCal)) {
			//within 3 months, we break it down by day with week & month shading?
			return DateDisplayTypes.Daily;
		}
		tempCal.setTime(startDate);
		tempCal.add(Calendar.YEAR, 2);
		if (endCal.before(tempCal)) {
			//within 2 years, we break it down by week with month shading
			return DateDisplayTypes.Weekly;
		}
		tempCal.setTime(startDate);
		tempCal.add(Calendar.YEAR, 4);
		
		if (endCal.before(tempCal)) {
			// 2-4 years break it down by month with year shading
			return DateDisplayTypes.Monthly;
		} else {  // if(endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR) >= 4) {
			//we need to break it down by year. with year shading
			return DateDisplayTypes.Yearly;
		} 
	}
	
	protected String getSelectionString(DateDisplayTypes displayType) {
		switch (displayType) {
			case Hourly:
				return "  strftime('%Y-%m-%d %H',time) ";
			case Daily:
				return " strftime('%Y-%m-%d', time) ";
			case Weekly:
				return " strftime('%Y-%W', time) ";
			case Monthly:
				return " strftime('%Y-%m',time) ";
			case Yearly:
				return " strftime('%Y',time) ";
			default:
				return "";
		
		}
	}

	protected String getLegendString(DateDisplayTypes displayType) {
		switch (displayType) {
		case Hourly:
			return "Hourly count";
		case Daily:
			return "Daily count";
		case Weekly:
			return "Weekly count";
		case Monthly:
			return "Monthly count";
		case Yearly:
			return "Annual count";
		default:
			return "";
		}
	}

	protected Date getNextValue(DateDisplayTypes displayType, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch (displayType) {
		case Hourly:
			cal.add(Calendar.HOUR, 1);
			break;
		case Daily:
			cal.add(Calendar.DATE, 1);
			break;
		case Weekly:
			cal.add(Calendar.WEEK_OF_YEAR, 1);
			break;
		case Monthly:
			cal.add(Calendar.MONTH, 1);
			break;
		case Yearly:
			cal.add(Calendar.YEAR, 1);
			break;
		default:
			throw new IllegalArgumentException("Bad display type: " + displayType); 
		}
		return cal.getTime();
	}

	protected boolean isBefore(DateDisplayTypes displayType, Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		if (cal2.before(cal1)) {
			return false;
		}
		// i really feel like there should be a cleaner way to do this but it escapes me
		if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) {
			return true;
		} else if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR) || displayType == DateDisplayTypes.Yearly	) {
			return false;
		}
		// we know the years are the same and we're comparing less than years
		if (cal1.get(Calendar.MONTH) < cal2.get(Calendar.MONTH)) {
			return true;
		} else if (cal1.get(Calendar.MONTH) > cal2.get(Calendar.MONTH) || displayType == DateDisplayTypes.Monthly) {
			return false;
		}
		// we know months and years are the same and we're comparing less than months
		if (cal1.get(Calendar.WEEK_OF_YEAR) < cal2.get(Calendar.WEEK_OF_YEAR)) {
			return true;
		} else if (cal1.get(Calendar.WEEK_OF_YEAR) > cal2.get(Calendar.WEEK_OF_YEAR) || displayType == DateDisplayTypes.Weekly) {
			return false;
		}
		// we know months, years, and weeks are the same and we're comparing less than weeks
		if (cal1.get(Calendar.DATE) < cal2.get(Calendar.DATE)) {
			return true;
		} else if (cal1.get(Calendar.DATE) > cal2.get(Calendar.DATE) || displayType == DateDisplayTypes.Daily) {
			return false;
		}
		// we know months, years,weeks, and days are the same and we're comparing less than days
		if (cal1.get(Calendar.HOUR) < cal2.get(Calendar.HOUR)) {
			return true;
		}  
		// anything else is not before
		return false;
	}

		
	protected Date getDate(DateDisplayTypes displayType, String string) {
		// TODO Auto-generated method stub
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date rawDate;
		try {
			rawDate = format.parse(string);
		} catch (ParseException e) {
			Log.d("ChartBroker","unparseable date: " + string);
			// this is actually a hard failure.  Just not sure what to do 
			return Constants.NULLDATE;
		}
		Calendar rawCal = Calendar.getInstance();
		rawCal.setTime(rawDate);
		Calendar calToReturn =Calendar.getInstance();
		rawCal.set(Calendar.MINUTE, 0);
		rawCal.set(Calendar.SECOND, 0);
		switch (displayType) {
		case Hourly:
			break;
		case Daily:
			rawCal.set(Calendar.HOUR, 0);
			break;
		case Weekly:
			rawCal.set(Calendar.HOUR, 0);
			rawCal.set(Calendar.DAY_OF_WEEK, 1);
			break;
		case Monthly:
			rawCal.set(Calendar.HOUR, 0);
			rawCal.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case Yearly:
			rawCal.set(Calendar.HOUR, 0);
			rawCal.set(Calendar.DAY_OF_MONTH, 1);
			rawCal.set(Calendar.MONTH, 1);
			break;
		default:
			return rawCal.getTime();
		}
		Date toReturn = calToReturn.getTime();
		Date reallyToReturn = rawCal.getTime();
		return reallyToReturn;

	}

	public void finishGraph() {
		mToggleThinkerHandler.post(mToggleThinker);		
		mTitleHandler.post(mUpdateActivityTitle);		
	}
	public abstract String getGraphTitle();
	
	public synchronized void setVariable(int id) {
		mChosenVariable = id;
		mGraphData = null;
		mGraphOptions = null;
	}
	
	public synchronized void setRange(Date startTime, Date endTime) {
		mStartDate = startTime;
		mEndDate = endTime;
		
		mGraphData = null;
		mGraphOptions = null;
	}
	
	public String[] getVariables() {
		return mVariableStrings;
	}
	public abstract String getName();
}
