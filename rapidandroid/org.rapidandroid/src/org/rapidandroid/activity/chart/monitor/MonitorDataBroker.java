package org.rapidandroid.activity.chart.monitor;

import java.util.Date;

import org.rapidandroid.activity.chart.ChartBroker;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 29, 2009 Summary: this class is unused. It potentially could be
 *          used to plot monitor data in the future.
 */
public class MonitorDataBroker extends ChartBroker {

	public MonitorDataBroker() {
		super(null, null, null, null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.ChartBroker#getGraphTitle()
	 */

	@Override
	public String getGraphTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.ChartBroker#loadGraph()
	 */

	@Override
	public void doLoadGraph() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.ChartBroker#getVariables()
	 */

	@Override
	public String[] getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.ChartBroker#setVariable(int)
	 */

	@Override
	public void setVariable(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRange(Date startTime, Date endTime) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rapidandroid.activity.chart.ChartBroker#finishGraph()
	 */
	@Override
	public void finishGraph() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "graph_monitor";
	}

}
