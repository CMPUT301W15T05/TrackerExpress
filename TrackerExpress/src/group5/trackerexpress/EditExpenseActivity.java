package group5.trackerexpress;

import java.io.File;
import java.text.SimpleDateFormat;
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
import android.text.InputType;
import android.app.DatePickerDialog.OnDateSetListener;
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
	
	/** The date. */
	private Date expenseDate;
	private DatePickerDialog expenseDialog;
	private SimpleDateFormat dateFormatter;
	

	/** The currency. */
	private Spinner category, currency;
	
	/** The img button. */
	private ImageButton imgButton;
	
	/** The create expense button. */
	private Button createExpenseButton;
	
	/** The flag check box. */
	private CheckBox flagCheckBox;
	
	/** The description, amount and date. */
	private EditText description, amount, dateEditText;
	
	/** The receipt uri. */
	private Uri receiptUri;
	
	/** The intent. */
	private Intent intent;
	
	/** The serialised id. */
	UUID serialisedId;// = (UUID) intent.getSerializableExtra("claimUUID");
	
	/** The claim. */
	Claim claim;// = Controller.getClaimList(EditExpenseActivity.this).getClaim(serialisedId);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		final Expense newExpense = new Expense("");
		System.out.println("STARTING INIT VARS");
		initializeVars();
		System.out.println("FINISHED INIT VARS");
		
		OnClickListener dateListener = new OnClickListener(){
			public void onClick(View v){
				setExpenseDate();
				expenseDialog.show();
			}
		};
		
		dateEditText.setOnClickListener(dateListener);
		
		OnClickListener picListener = new OnClickListener() {
			public void onClick(View v) {
				takeAPhoto();
			}
		};
		imgButton.setOnClickListener(picListener);
		
		OnClickListener statusListener = new OnClickListener(){
			public void onClick(View v){
				
			}
		};
		flagCheckBox.setOnClickListener(statusListener);
			
		OnClickListener createListener = new OnClickListener() {
			public void onClick(View v) {
				editExpense(newExpense);
			}
		};
		createExpenseButton.setOnClickListener(createListener);
	}
	
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

		ImageButton button = (ImageButton) findViewById(R.id.editExpenseTakeAPhoto);
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
		intent = this.getIntent();
		serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
		claim = Controller.getClaimList(EditExpenseActivity.this).getClaim(serialisedId);
		
		description = (EditText) findViewById(R.id.editExpenseDescription);
		amount = (EditText) findViewById(R.id.editExpenseAmount);
		imgButton = (ImageButton) findViewById(R.id.editExpenseTakeAPhoto);
		dateEditText = (EditText) findViewById(R.id.editExpenseDate);
		dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
		
		category = (Spinner) findViewById(R.id.editExpenseCategorySpinner);
		ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource 
				(this,  R.array.category_array,  android.R.layout.simple_spinner_item); //create array adapter using string array and default spinner layout
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //specify layout to use when list of choices appears
		category.setAdapter(categoryAdapter);
		
		currency = (Spinner) findViewById(R.id.editExpenseCurrencySpinner);
		ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource
				(this,  R.array.currency_array,  android.R.layout.simple_spinner_item); //create array adapter using string array and default spinner layout
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //specify layout to use when list of choices appears
		category.setAdapter(currencyAdapter);
		
		createExpenseButton = (Button) findViewById(R.id.editExpenseCreateExpenseButton);
		flagCheckBox = (CheckBox) findViewById(R.id.editExpenseIncompleteCheckBox);
		
	}

	private void setExpenseDate() {
		Calendar date = Calendar.getInstance();
		expenseDialog = new DatePickerDialog(this, new OnDateSetListener() {
			 
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                dateEditText.setText(dateFormatter.format(date.getTime()));
            }
        },date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		
		expenseDate.setMM(date.get(Calendar.MONTH));
		expenseDate.setDD(date.get(Calendar.DAY_OF_MONTH));
		expenseDate.setYYYY(date.get(Calendar.YEAR));
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
		
		expense.setDate(this, expenseDate);
		
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
