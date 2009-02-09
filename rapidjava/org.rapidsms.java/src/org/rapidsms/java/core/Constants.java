/*
 *    rapidsdms-java - Java libraries for RapidSMS
 *    Copyright (C) 2009 Dimagi Inc., UNICEF
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.rapidsms.java.core;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Daniel Myung dmyung@dimagi.com
 * @created Feb 2, 2009 Summary:
 */
public class Constants {
	public static final Date NULLDATE = new GregorianCalendar(1900, 1, 1, 5, 0).getTime();
	public static final Calendar NULLCALENDAR = new GregorianCalendar(1900, 1, 1, 5, 0);
}
