package group5.trackerexpress;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class EditableActivity extends Activity implements DatePickerFragment.TheListener{
	
	private Map<EditText, TextWatcher> viewMap = new HashMap<EditText, TextWatcher>();

	//TODO: write up the javadocs - Jesse
	//TODO: Editable classes possibly extend this class? If this is only used with editclaim, then merge with it
	public void limitLength(EditText view, int length) {
		limitLength(view, length, "Only up to " + length + " chars");
	}
	
	public abstract class basicWatcher implements TextWatcher {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}
		
		@Override
		public abstract void afterTextChanged(Editable s);
	};
	
	
	public void limitLength(final EditText view, final int length, final String message) {
		
		TextWatcher lengthWatcher = new basicWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == length + 1) {
					view.setError(message);
					s.delete(length, length + 1);
				}
			}
		};
		
		view.addTextChangedListener(lengthWatcher);
		final CharSequence hintText = view.getHint();
		final CharSequence extraHint = " (at most " + length + " chars)";
		
		view.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					view.setHint(hintText);
				} else {
					view.setHint(Html.fromHtml(hintText + "<small>" + extraHint + "</small>"));
				}
				
			}
		});
		
		viewMap.put(view, lengthWatcher);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		// http://stackoverflow.com/a/1066607/4269270 21/03/2015
		for (Map.Entry<EditText, TextWatcher> entry : viewMap.entrySet())
		{
		    entry.getKey().removeTextChangedListener(entry.getValue());
		}
		
		viewMap.clear();
	}
	
	public void cancelCheck(final Context context){
		AlertDialog.Builder helperBuilder = new AlertDialog.Builder(context);
		helperBuilder.setCancelable(false);
		helperBuilder.setTitle("Warning");
		helperBuilder.setMessage("Are you sure you want to exit before saving?");
		helperBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener(){
			
			/** make Cancel button clickable
			 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
			 */
			public void onClick(DialogInterface dialog, int which){
								
				Toast.makeText(context, "Canceling", Toast.LENGTH_SHORT).show();
				finish();
				}
			});
			
		helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
						
			/** Do Nothing, return to EditClaimActivity
			 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
			 */
			@Override
			public void onClick(DialogInterface dialog, int which){
				}
			});
		AlertDialog helpDialog = helperBuilder.create();
		helpDialog.show();
	}

	public void showDatePickerDialog(View v, Calendar dateSelection) {
	    DialogFragment dateFragment = new DatePickerFragment(v, dateSelection);
	    dateFragment.show(getFragmentManager(), "datePicker");
	}

	public void returnDate(View view, Calendar date) {
	}

}
