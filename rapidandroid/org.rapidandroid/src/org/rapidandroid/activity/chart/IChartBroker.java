package org.rapidandroid.activity.chart;

import java.util.Calendar;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 29, 2009
 * Summary:
 */
public interface IChartBroker {
	void loadGraph();
	String getGraphTitle();
	String[] getVariables();
	void setVariable(int id);
	void setRange(Calendar startTime, Calendar endTime);
	
}
