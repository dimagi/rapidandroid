
package org.rapidandroid.view;

import org.rapidandroid.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * 
 *  
 * @author Daniel Myung dmyung@dimagi.com
 * @created Jan 9, 2009
 * 
 * 
 *   
 */

public class MessageView extends LinearLayout {
	public MessageView(Context context, AttributeSet attrs) { 
			super(context); 
			LayoutInflater inflate = LayoutInflater.from(context);
			inflate.inflate(R.layout.message_view,this);			 
	}
}
