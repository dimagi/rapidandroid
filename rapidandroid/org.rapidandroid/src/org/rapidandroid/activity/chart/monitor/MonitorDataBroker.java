/*
 * Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
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
	public void jsFinishGraph() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return "graph_monitor";
	}

}
