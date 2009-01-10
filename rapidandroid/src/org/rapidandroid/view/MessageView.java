/**
 * 
 */
package org.rapidandroid.view;

import java.util.Map;

import org.rapidandroid.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * @author dmyung
 * @created Jan 9, 2009
 */
public class MessageView extends LinearLayout {
	public MessageView(Context context, AttributeSet attrs) { 
			super(context); 
			LayoutInflater inflate = LayoutInflater.from(context);
			inflate.inflate(R.layout.message_view,this); 
			//... 
			//      Here parse the custom attributes for my custom widget and
			// potentially do something with them, but it is not needed... 
			//...
			 
	}
}
