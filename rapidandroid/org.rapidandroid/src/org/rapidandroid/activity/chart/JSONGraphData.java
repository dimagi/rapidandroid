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
