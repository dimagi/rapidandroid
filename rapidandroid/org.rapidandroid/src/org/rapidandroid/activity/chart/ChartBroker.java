package org.rapidandroid.activity.chart;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rapidandroid.data.SmsDbHelper;
import org.rapidsms.java.core.Constants;

import android.app.Activity;
import android.os.Handler;
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
	
	protected ChartBroker(Activity activity, WebView appView, Date startDate, Date endDate) {
		mParentActivity = activity;
		mAppView = appView;
		rawDB = new SmsDbHelper(appView.getContext());
		mVariableStrings = new String[] { "Trends by day", "Receipt time of day" };
		mStartDate = startDate;
		mEndDate= endDate;
	}
	
	
	
	protected abstract void doLoadGraph();
	
	/**
	 * This is the primary method that the JavaScript in our HTML form will need access to in order to display graph data. 
	 */
	public final void loadGraph() {
		mToggleThinkerHandler.post(mToggleThinker);
		doLoadGraph();
		loadGraphFinish();
	}
	protected void loadGraphFinish(){
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
	
	public void finishGraph() {
		mToggleThinkerHandler.post(mToggleThinker);		
		mTitleHandler.post(mUpdateActivityTitle);		
	}
	public abstract String getGraphTitle();
	
	public void setVariable(int id) {
		mChosenVariable = id;
	}
	
	public void setRange(Date startTime, Date endTime) {
		mStartDate = startTime;
		mEndDate = endTime;
	}
	
	public String[] getVariables() {
		return mVariableStrings;
	}
	public abstract String getName();
}
