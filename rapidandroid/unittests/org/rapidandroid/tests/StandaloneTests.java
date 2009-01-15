/**
 * 
 */
package org.rapidandroid.tests;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * @author dmyung
 * @created Jan 14, 2009
 */
public class StandaloneTests extends AndroidTestCase {

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
	public void testSimple() {
		System.out.println("Test HEllo");
		assertEquals(true, true);
	}
	
	public void testContext() {
		System.out.println("Test Context");
		Context c = getContext();
		assertTrue(true);
	}

}
