package group5.trackerexpress;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

// TODO: Auto-generated Javadoc
/**
 * The Class EditExpenseActivity.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class EditExpenseActivity extends Activity {
	
	static final int dialogId = 1;
	
	int y, d, m;
	
	/** The currency. */
	private Spinner category, currency;
	
	/** The img button. */
	private ImageButton imgButton;
	
	/** The create expense button. */
	private Button createExpenseButton;
	
	/** The flag check box. */
	private CheckBox flagCheckBox;
	
	/** The description, amount and date. */
	private EditText description, amount;
	
	/** The receipt uri. */
	private Uri receiptUri;
	
	/** The intent. */
	final Intent intent = this.getIntent();
	
	/** The serialised id. */
	UUID serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
	
	/** The claim. */
	final Claim claim = Controller.getClaimList(EditExpenseActivity.this).getClaim(serialisedId);
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		Calendar today = Calendar.getInstance();
		y = today.get(Calendar.YEAR);
		d = today.get(Calendar.DAY_OF_MONTH);
		m = today.get(Calendar.MONTH);
		showDialog(dialogId);
		
		final Expense newExpense = new Expense("");
		
		initializeVars();
		
		OnClickListener picListener = new OnClickListener() {
			public void onClick(View v) {
				takeAPhoto();
			}
		};
		imgButton.setOnClickListener(picListener);
			
		
		OnClickListener createListener = new OnClickListener() {
			public void onClick(View v) {
				editExpense(newExpense);
			}
		};
		createExpenseButton.setOnClickListener(createListener);
	}
	
	protected Dialog onCreateDialog(int id){
		switch(id){
			case dialogId:
				return new DatePickerDialog(this, mDateSetListener, y, m, d);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener(){
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
			y = year;
			m = monthOfYear;
			d = dayOfMonth;
		}
		
	};
	
	
	

	/** The Constant CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE. */
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;


	/**
	 * Take a photo.
	 */
	public void takeAPhoto() {
		// TODO: Create an intent with the action
		// MediaStore.ACTION_IMAGE_CAPTURE

		// ComponentName cn = new ComponentName("es.softwareprocess.bogopicgen",
		// "es.softwareprocess.bogopicgen.BogoPicGenActivity");
		// ComponentName cn = new ComponentName("com.android.camera",
		// "com.android.camera.Camera");
		// intent.setComponent(cn);

		// Create a folder to store pictures
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();

		ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
		Drawable photo = Drawable.createFromPath(receiptUri.getPath());
		button.setImageDrawable(photo);
		}

		// Create an URI for the picture file
		String imageFilePath = folder + "/"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		File imageFile = new File(imageFilePath);
		receiptUri = Uri.fromFile(imageFile);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, receiptUri);

		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		// TODO: Put in the intent in the tag MediaStore.EXTRA_OUTPUT the URI

		// TODO: Start the activity (expecting a result), with the code
		// CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE

	}

	/**
	 * Initialize variables.
	 */
	private void initializeVars() {
		// TODO Auto-generated method stub
		description = (EditText) findViewById(R.id.editDescription);
		amount = (EditText) findViewById(R.id.editAmount);
		imgButton = (ImageButton) findViewById(R.id.TakeAPhoto);
		
		
		category = (Spinner) findViewById(R.id.categorySpinner);
		ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource 
				(this,  R.array.category_array,  android.R.layout.simple_spinner_item); //create array adapter using string array and default spinner layout
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //specify layout to use when list of choices appears
		category.setAdapter(categoryAdapter);
		
		currency = (Spinner) findViewById(R.id.currencySpinner);
		ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource
				(this,  R.array.currency_array,  android.R.layout.simple_spinner_item); //create array adapter using string array and default spinner layout
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //specify layout to use when list of choices appears
		category.setAdapter(currencyAdapter);
		
		createExpenseButton = (Button) findViewById(R.id.createExpenseButton);
		flagCheckBox = (CheckBox) findViewById(R.id.incompleteCheckBox);
		
	}
	
	private void editExpense(final Expense expense) {
		
		String title = description.getText().toString();
		expense.setTitle(this, title);
		
		Double money = Double.parseDouble(amount.getText().toString());
		expense.setAmount(this, money);
		
		String categorySelection = category.getSelectedItem().toString();
		expense.setCategory(this, categorySelection);
		
		String currencySelection = currency.getSelectedItem().toString();
		expense.setCurrency(this, currencySelection);
		
		expense.setDate(this, y, m, d);
		
	}
	
	/**
	 * On item selected.
	 *
	 * @param parent the parent
	 * @param view the view
	 * @param position the position
	 * @param id the id
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
		parent.getItemAtPosition(position);
    }

    /**
     * On nothing selected.
     *
     * @param parent the parent
     */
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}
