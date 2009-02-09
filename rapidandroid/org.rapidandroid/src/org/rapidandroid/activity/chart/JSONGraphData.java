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
