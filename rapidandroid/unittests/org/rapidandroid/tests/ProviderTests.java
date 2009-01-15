/**
 * 
 */
package org.rapidandroid.tests;

import org.rapidandroid.activity.Dashboard;
import org.rapidandroid.data.RapidSmsContentProvider;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase;


/**
 * @author dmyung
 * @created Jan 14, 2009
 */
public class ProviderTests extends android.test.ProviderTestCase<RapidSmsContentProvider> {
	
	
	public ProviderTests() {
		super(RapidSmsContentProvider.class, "org.rapidandroid.rapidandroiddata");
	}
	
		/**
	 * @param providerClass
	 * @param providerAuthority
	 */
//	public ProviderTests(Class<RapidSmsContentProvider> providerClass,
////			String providerAuthority) {
////		
////		// TODO Auto-generated constructor stub
//	}

		protected void setUp() throws Exception {		
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {		
		super.tearDown();
	}
	
	public void testSimple() {
		assertFalse(false);
	}
	
	public void testProviderExists() {
		//ntext c = getMockContext();
		//Context context = getContext();	
		//assertTrue(false);
		RapidSmsContentProvider prov = getProvider();
		
		
	}
}
