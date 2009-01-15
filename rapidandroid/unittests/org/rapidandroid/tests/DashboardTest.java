/**
 * 
 */
package org.rapidandroid.tests;

import org.rapidandroid.activity.Dashboard;

import android.test.ActivityInstrumentationTestCase;

/**
 * @author dmyung
 * @created Jan 14, 2009
 */
public class DashboardTest extends ActivityInstrumentationTestCase<Dashboard> {

	public DashboardTest() {
		super("org.rapidandroid.activity", Dashboard.class);
	}
	
	public DashboardTest(String pkg, Class<Dashboard> activityClass) {
		super(pkg, activityClass);
		// TODO Auto-generated constructor stub
	}
	

}
