package group5.trackerexpress;


import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	private TheListener listener;
	private Calendar selectedDate;
	private View view;
	
	public DatePickerFragment(View v, Calendar date) {
		selectedDate = date;
		this.view = v;
	}

	public DatePickerFragment(View v) {
		selectedDate = Calendar.getInstance();
		this.view = v;
	}

	public interface TheListener {
		public void returnDate(View view, Calendar date);
	}
	

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

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {

		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		if (listener != null) {
			listener.returnDate(this.view, c);
		}
    }
	
}