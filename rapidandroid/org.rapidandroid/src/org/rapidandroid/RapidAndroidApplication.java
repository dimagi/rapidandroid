package org.rapidandroid;

import android.app.Application;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 27, 2009 Summary:
 */
public class RapidAndroidApplication extends Application {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Debug.startMethodTracing("rapidandroid_application");
		ModelBootstrap.InitApplicationDatabase(this.getApplicationContext());

	}

}
