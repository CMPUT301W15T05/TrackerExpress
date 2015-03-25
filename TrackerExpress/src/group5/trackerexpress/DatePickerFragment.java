package group5.trackerexpress;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	private TheListener listener;
	private Date selectedDate;
	
	public DatePickerFragment(Date date) {
		selectedDate = date;
	}
	
	public DatePickerFragment() {
		selectedDate = new Date();
	}

	public interface TheListener {
		public void returnDate(Date date);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
	
		int year = selectedDate.getYYYY();
		int month = selectedDate.getMM();
		int day = selectedDate.getDD();
		
		listener = (TheListener) getActivity();

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {

		Date date = new Date(year, month, day);
		if (listener != null) {
			listener.returnDate(date);
		}
    }
	
}