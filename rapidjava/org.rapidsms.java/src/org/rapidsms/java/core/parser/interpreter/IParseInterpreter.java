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
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 23, 2009
 * Summary:
 */
package org.rapidsms.java.core.parser.interpreter;

/**
 * @author dmyung
 * @created Jan 23, 2009
 */
public interface IParseInterpreter {
	Object interpretValue(String token);
}
