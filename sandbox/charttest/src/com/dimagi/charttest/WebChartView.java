package com.dimagi.charttest;

import java.io.InputStream;


import android.app.Activity;
import android.os.Bundle;
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
        wv.getSettings().setJavaScriptEnabled(true); 
       
        wv.loadUrl("file:///android_asset/flot/html/index.html");
       
    }
}