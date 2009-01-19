/**
 * 
 */
package org.rapidsms.java.core.parser;

import java.util.HashMap;

import org.rapidsms.java.core.model.Field;
import org.rapidsms.java.core.model.Form;

/**
 * @author dmyung
 * @created Jan 16, 2009
 */
public class ParseResult {

		private static final String NULL = "(null)";
		
		private HashMap<Field,String> results;
		
		public ParseResult(Form form) {
			this.results = new HashMap<Field,String>();
			
			Field[] fields = form.getFields();
			int fieldcount = fields.length;
			for(int i = 0; i < fieldcount; i++) {
				if(this.results.containsKey(fields[i])) {
					throw new IllegalArgumentException(
							"Error, form definition has duplicate fields in definition, a troublesome condition.  " +
									"FieldID: " + fields[i].getFieldId() + 
									" Name: " + fields[i].getName());					
				}
				
				this.results.put(fields[i], NULL);
			}
		}
		
		public void putResult(Field field, String value) {
			if(!results.containsKey(field)) {
				throw new IllegalArgumentException(
						"Error, form definition does not contain field in definition." +
								"FieldID: " + field.getFieldId() + 
								" Name: " + field.getName());		
			} else if (results.containsKey(field) && !results.get(field).equals(NULL)) {
				throw new IllegalArgumentException(
						"Error, this field has already been parsed with a result." +
								"FieldID: " + field.getFieldId() + 
								" Name: " + field.getName());
			} else {
				//ok, passed simple sanity checkes, let's set the value
				
				results.put(field,value);
			}
		}
		
		
		public HashMap<Field,String> getResults() {
			return results;
		}
		
}
