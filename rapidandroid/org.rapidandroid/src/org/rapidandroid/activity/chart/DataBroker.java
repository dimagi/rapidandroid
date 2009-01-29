package org.rapidandroid.activity.chart;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.webkit.WebView;

public class DataBroker {
	private WebView mAppView;

	public DataBroker(WebView appView) {
		this.mAppView = appView;
		//mAppView.getGlobalVisibleRect(r)
	}

	public void loadGraph() {
		JSONArray arr = new JSONArray();

		JSONObject result = new JSONObject();
		try {
			result.put("data", getRawDataJSON());
			result.put("lines", getLineOptionsJSON());
			result.put("points", getFalseJSON());
			arr.put(result);

		} catch (Exception ex) {
			//
		}
		int width = mAppView.getWidth();
		int height = 0;
		if(width == 480) {
			height=320;
		} else if(width == 320) {
			height=480;
		}		
		height = height-50;
		
		mAppView.loadUrl("javascript:SetGraph(\"" + width+"px\", \"" + height + "px\")");
		mAppView.loadUrl("javascript:GotGraph(" + arr.toString() + ")");
	}
	

	private JSONArray getRawDataJSON() {
		Random rand = new Random();
		JSONArray arr = new JSONArray();
		int priorval = rand.nextInt(100);
		for (int i = 0; i < 100; i++) {
			JSONArray elem = new JSONArray();
			elem.put(i);
			if(rand.nextBoolean()) {
				priorval += rand.nextInt(10);
			} else {
				priorval -= rand.nextInt(10);
			}
			elem.put(priorval);
			arr.put(elem);
		}
		return arr;
	}

	private JSONObject getLineOptionsJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("show", true);
		} catch (Exception ex) {

		}
		return ret;
	}
	
	private JSONObject getFalseJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("show", false);
		} catch (Exception ex) {

		}
		return ret;
	}

	public String getGraphTitle() {
		return "my line baby";
	}

	//THIS ALSO WORKS!!
	public String getGraphTitle(String arg, int intarg) {
		return "my line baby: " + arg.length() * intarg;
	}
}
