package org.rapidandroid.activity.chart;

import java.util.Date;

import android.app.Activity;


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
public interface IChartBroker {
	/**
	 * This is the primary method that the JavaScript in our HTML form will need access to in order to display graph data. 
	 */
	void loadGraph();
	void finishGraph();
	String getGraphTitle();
	String[] getVariables();
	void setVariable(int id);
	void setRange(Date startTime, Date endTime);	
}
