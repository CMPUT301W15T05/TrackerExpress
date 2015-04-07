package group5.trackerexpress;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

/**
 * Creates a row table. Needed by Map activities and view claim fragment.
 * @author crinklaw
 */
public class RowTableLayout {
	private View rootView;
	private Context context;
	
	private int smallPadding = 0;
	private int largePadding = 0;
	
	private LayoutParams trlp = 
					new LayoutParams(LayoutParams.MATCH_PARENT, 
									LayoutParams.WRAP_CONTENT);
	
	public RowTableLayout(View rootView, Context context) {
		this.rootView = rootView;
		this.context = context;
	}
	
	public void setPadding(int smallPadding, int largePadding) {
		this.smallPadding = smallPadding;
		this.largePadding = largePadding;
	}

	public void insertRow(int viewID, String text, boolean newSection) {
		Spanned stext = new SpannableStringBuilder(text);
		insertRow(viewID, stext, newSection);
	}
	
	public void insertRow(int viewID, Spanned text, boolean newSection) {
		// Create a new row to be added
		TableLayout tl = (TableLayout) rootView.findViewById(viewID);
		
		TableRow tr = new TableRow(context);
		tr.setLayoutParams(trlp);
		
		int padding = 0;
		if (newSection) {
			padding = smallPadding;
		} else {
			padding = largePadding;
		}

		tr.setPadding(0, padding, 0, 0);
		
		// Create the TextView to be added to the row content
		TextView tv = new TextView(context);
		if (newSection) {
			tv.setTextSize(20);
		} else {
			tv.setTextSize(16);
		}
		tv.setText(text);
		tv.setLayoutParams(trlp);
		
		// Add TextView to row
		tr.addView(tv);
		
		tl.addView(tr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
}