package group5.trackerexpress;


import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

/**
 * The DatePickerFragment makes getting a date from a date picker a
 * 		task of ease.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 *
 */
@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	/** Listener to return date */
	private TheListener listener;
	
	/** The selected date of the date fragment */
	private Calendar selectedDate;
	
	/** The view of datepicker */
	private View view;
	
	/** 
	 * Constructer to create the DatePicker object
	 * 
	 * @param v: view of date picker
	 * @param date: date to set datepicker to
	 */
	public DatePickerFragment(View v, Calendar date) {
		selectedDate = date;
		this.view = v;
	}

	/**
	 * Costructer to create the DatePicker object
	 * 
	 * @param v: view of the date picker
	 */
	public DatePickerFragment(View v) {
		selectedDate = Calendar.getInstance();
		this.view = v;
	}

	/**
	 * The interface that makes this fragment implement
	 * 		a returnDate function
	 */
	public interface TheListener {
		public void returnDate(View view, Calendar date);
	}
	

	/**
	 * Creates a new DatePicker dialog and returns it
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
	
		int year = selectedDate.get(Calendar.YEAR);
		int month = selectedDate.get(Calendar.MONTH);
		int day = selectedDate.get(Calendar.DAY_OF_MONTH);
		
		listener = (TheListener) getActivity();

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	/** 
	 * Sets the date on the date picker
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {

		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		if (listener != null) {
			listener.returnDate(this.view, c);
		}
    }
	
}