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
package org.rapidandroid.activity.chart;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Data class for storing options of a JSON Graph
 * 
 * @author Cory Zue
 * 
 */
public class JSONGraphData {

	private JSONArray _data;
	private JSONObject _options;

	public JSONGraphData(JSONArray data, JSONObject options) {
		_data = data;
		_options = options;
	}

	/**
	 * Get the data
	 * 
	 * @return
	 */
	public JSONArray getData() {
		return _data;
	}

	/**
	 * Get the options
	 * 
	 * @return
	 */
	public JSONObject getOptions() {
		return _options;
	}
}
