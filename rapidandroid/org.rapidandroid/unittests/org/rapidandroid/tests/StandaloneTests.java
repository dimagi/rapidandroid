/*
 * Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

/**
 * 
 */
package org.rapidandroid.tests;

import android.test.AndroidTestCase;

/**
 * @author dmyung
 * @created Jan 14, 2009
 */
public class StandaloneTests extends AndroidTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.AndroidTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	public void testSimple() {
		System.out.println("Test Hello");
		assertEquals(true, true);
	}

	public void testContext() {
		// System.out.println("Test Context");
		// Context c = getContext();
		// assertTrue(true);
	}

}
