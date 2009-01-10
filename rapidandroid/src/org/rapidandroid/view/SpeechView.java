/**
 * 
 */
package org.rapidandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author dmyung
 * @created Jan 9, 2009
 */
public class SpeechView extends LinearLayout {
	/**
	 * We will use a SpeechView to display each speech. It's just a LinearLayout
	 * with two text fields.
	 * 
	 */

	public SpeechView(Context context, String title, String dialogue,
			boolean expanded) {
		super(context);
		this.setOrientation(VERTICAL);

		// Here we build the child views in code. They could also have
		// been specified in an XML file.

		mTitle = new TextView(context);
		mTitle.setText(title);
		addView(mTitle, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		mDialogue = new TextView(context);
		mDialogue.setText(dialogue);
		addView(mDialogue, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mDialogue.setVisibility(expanded ? VISIBLE : GONE);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SpeechView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * @param context
	 */
	public SpeechView(Context context) {
		super(context);

	}

	/**
	 * Convenience method to set the title of a SpeechView
	 */
	public void setTitle(String title) {
		mTitle.setText(title);
	}

	/**
	 * Convenience method to set the dialogue of a SpeechView
	 */
	public void setDialogue(String words) {
		mDialogue.setText(words);
	}

	/**
	 * Convenience method to expand or hide the dialogue
	 */
	public void setExpanded(boolean expanded) {
		mDialogue.setVisibility(expanded ? VISIBLE : GONE);
	}

	private TextView mTitle;
	private TextView mDialogue;
}
