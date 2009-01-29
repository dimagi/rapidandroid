package org.rapidandroid.activity.chart;

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
}
