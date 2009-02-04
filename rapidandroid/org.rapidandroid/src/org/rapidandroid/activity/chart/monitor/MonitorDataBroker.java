package org.rapidandroid.activity.chart.monitor;


import java.util.Date;

import org.rapidandroid.activity.chart.ChartBroker;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 29, 2009
 * Summary:
 */
public class MonitorDataBroker extends ChartBroker {

	public MonitorDataBroker() {
		super(null,null,null,null);
		
	}
	
	/* (non-Javadoc)
	 * @see org.rapidandroid.activity.chart.ChartBroker#getGraphTitle()
	 */
	
	public String getGraphTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rapidandroid.activity.chart.ChartBroker#loadGraph()
	 */
	
	public void doLoadGraph() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.rapidandroid.activity.chart.ChartBroker#getVariables()
	 */
	
	public String[] getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rapidandroid.activity.chart.ChartBroker#setVariable(int)
	 */
	
	public void setVariable(int id) {
		// TODO Auto-generated method stub
		
	}

	public void setRange(Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.rapidandroid.activity.chart.ChartBroker#finishGraph()
	 */
	public void finishGraph() {
		// TODO Auto-generated method stub
		
	}
	
	public String getName() {
		return "graph_monitor";
	}

}
