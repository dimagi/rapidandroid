/*
 *    rapidandroid - SMS gateway for the android platform
 *    Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
