/**
 * 
 */
package org.rapidandroid.tests;

import org.rapidandroid.data.RapidSmsContentProvider;
import org.rapidandroid.data.RapidSmsDataDefs;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author dmyung
 * @created Jan 14, 2009
 */
public class ProviderTests extends
		android.test.ProviderTestCase<RapidSmsContentProvider> {

	private RapidSmsContentProvider mProv;
	private Uri currUri;
	
	public ProviderTests() {
		super(RapidSmsContentProvider.class,
				"org.rapidandroid.rapidandroiddata");
	}

	protected void setUp() throws Exception {
		super.setUp();

		mProv = getProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSimple() {
		assertFalse(false);
	}

	public void testProviderGetTypeBase() {
		assertEquals(mProv.getType(RapidSmsDataDefs.Message.CONTENT_URI),RapidSmsDataDefs.Message.CONTENT_TYPE);
		assertEquals(mProv.getType(RapidSmsDataDefs.Monitor.CONTENT_URI),RapidSmsDataDefs.Monitor.CONTENT_TYPE);		
	}
	
	public void testProviderGetTypeID() {
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/message/1")),RapidSmsDataDefs.Message.CONTENT_ITEM_TYPE);
		assertEquals(mProv.getType(Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/monitor/1")),RapidSmsDataDefs.Monitor.CONTENT_ITEM_TYPE);		
	}
	
	public void testMonitorInsertAndQuery() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(RapidSmsDataDefs.Monitor.PHONE,"6176453236");		
		currUri = mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
		
		Cursor cr = mProv.query(currUri, null, null, null, null);
		assertEquals(1, cr.getCount());
	}
	
	public void testMonitorInsertAndVerifyCounts() {
		int count = 100;
		int baseline = 0;
		Log.w("ProviderTests.testMonitorInsertAndVerifyCounts", "flasjdfklasdjf");
		Uri monitorquery = Uri.parse("content://" + RapidSmsDataDefs.AUTHORITY + "/monitor");
		Cursor cr = mProv.query(monitorquery, null, null, null, null);
		baseline = cr.getCount();
		
		for(int i = 0; i < count; i++) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(RapidSmsDataDefs.Monitor.PHONE,"55544433" + i);		
			mProv.insert(RapidSmsDataDefs.Monitor.CONTENT_URI, initialValues);
		}
		
		Cursor cr2 =mProv.query(monitorquery, null, null, null, null);
		assertEquals(baseline+100, cr2.getCount());		
        
	}
	
	
}
