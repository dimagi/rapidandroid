package com.dimagi.charttest;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

public class BarData {
	private WebView mAppView;    
    
    
	public BarData(WebView appView) {        
        this.mAppView = appView;     
    }
	
		
	public void loadGraph() {
		//[[0, 3], [4, 8], [8, 5], [9, 13]];
		JSONArray arr = new JSONArray();
		
		JSONObject result = new JSONObject();
		try {
			result.put("data", getRawDataJSON());
			result.put("bars", getBarOptionsJSON());
			
			
			arr.put(result);
		
		} catch (Exception ex) {
			//
		}

		Log.d("graphdebug", "GetGraphJSON()");
		
		//String ret = "var data = " + arr.toString() + ";";
		//Log.d("graphdebug", ret);
		mAppView.loadUrl("javascript:GotGraph(" + arr.toString() + ")");
		
	}
	
	
	private JSONArray getRawDataJSON() {
		Random rand = new Random();
		JSONArray arr = new JSONArray();
		for(int i = 0; i < 7; i++) {
			JSONArray elem = new JSONArray();
			elem.put(i);
			elem.put(rand.nextInt(15));
			arr.put(elem);
		}    		
		return arr;    		
	}
	
	private JSONObject getBarOptionsJSON() {
		JSONObject ret = new JSONObject();    		
		try {    		
			ret.put("show", true);    		
		} catch (Exception ex) {
			
		}
		return ret;
	}
	
	public String getGraphTitle() {
		return "my bar graph baby!";
	}
    
}
