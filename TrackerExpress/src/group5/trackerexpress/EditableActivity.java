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

/**
 * Superclass for the edit activities (e.g. EditClaim and EditExpense)
 * Contains a bunch of useful attributes and functions
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 *
 */
public class EditableActivity extends Activity implements DatePickerFragment.TheListener{
	
	/** Text watchers for each of the EditTexts ( checks if the input is valid ) */
	private Map<EditText, TextWatcher> viewMap = new HashMap<EditText, TextWatcher>();

	/** 
	 * limits the length of the view. Will show errors if length exceeeded.
	 * 
	 * @param view: EditText in question
	 * @param length: maximum length of EditText
	 */
	public void limitLength(EditText view, int length) {
		// Calls the more in depth version of limitLength
		limitLength(view, length, "Only up to " + length + " chars");
	}
	
	/**
	 * Watcher class for each of the TextViews
	 */
	public abstract class basicWatcher implements TextWatcher {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}
		
		@Override
		public abstract void afterTextChanged(Editable s);
	};
	
	
	/**
	 * Places a text watcher on a specific view with a specific error message
	 * 
	 * @param view: EditText to watch
	 * @param length: max length of EditText
	 * @param message: error message to show if max length exceeded
	 */
	public void limitLength(final EditText view, final int length, final String message) {
		// Get the text watcher for the EditText
		TextWatcher lengthWatcher = new basicWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// find out if the EditText has too long of a string in it
				if (s.length() == length + 1) {
					view.setError(message);
					s.delete(length, length + 1);
				}
			}
		};
		
		// add the text watcher to the view
		view.addTextChangedListener(lengthWatcher);
		
		// Get the hint of the text
		final CharSequence hintText = view.getHint();
		
		// Adds more to the hint of the text (tells max length)
		final CharSequence extraHint = " (at most " + length + " chars)";
		
		// If the edittext is no longer the focus,
		// 		show hint/hint+extrahint depending on focus
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
	
	// On Stop clear all the views wilth their trackers
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
	
	/** 
	 * Function is called when user exits without saving to
	 * 		ask if they truly do not want to save
	 * 
	 * @param context: for retriving proper model information
	 */
	public void cancelCheck(final Context context){
		// All this is, is just constructing a dialog box
		// asking if the want to quit the Edit Activity
		AlertDialog.Builder helperBuilder = new AlertDialog.Builder(context);
		helperBuilder.setCancelable(false);
		helperBuilder.setTitle("Warning");
		helperBuilder.setMessage("Are you sure you want to exit before saving?");
		helperBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener(){
			
			/** make Cancel button clickable
			 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
			 */
			public void onClick(DialogInterface dialog, int which){
				// Indicate the saving is cancelled and exit if that is what the user wishes
				Toast.makeText(context, "Canceling", Toast.LENGTH_SHORT).show();
				finish();
				}
			});
			
		helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			// Return the edit Activity if that is what the user wishes
			@Override
			public void onClick(DialogInterface dialog, int which){
				}
			});
		AlertDialog helpDialog = helperBuilder.create();
		helpDialog.show();
	}

	/**
	 * Show the date picker dialog with the given date
	 * 
	 * @param v: view of the dialog
	 * @param dateSelection: Date to display
	 */
	public void showDatePickerDialog(View v, Calendar dateSelection) {
	    DialogFragment dateFragment = new DatePickerFragment(v, dateSelection);
	    dateFragment.show(getFragmentManager(), "datePicker");
	}

	/**
	 * gets the date from the given datepicker
	 */
	public void returnDate(View view, Calendar date) {
	}

}
