package com.dimagi.charttest;



import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class WebChartView extends Activity {
    /** Called when the activity is first created. */
	
	final String mimeType = "text/html"; 
	final String encoding = "utf-8"; 
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        WebView wv = (WebView) findViewById(R.id.wv1);
         

        
       
        //BarData bd = new BarData(wv);
        LineData bd = new LineData(wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(bd, "graphdata");
        wv.setWebChromeClient(new MyWebChromeClient());
        wv.loadUrl("file:///android_asset/flot/html/dynamic.html");
        //wv.load
     
        wv.debugDump();
    }
    
    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
      
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("graphdebug", message);
            result.confirm();
            return true;
        }
    }
    
    
}