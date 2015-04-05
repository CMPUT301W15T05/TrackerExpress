package group5.trackerexpress;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Allows creation and editing of an expense. Editing is just like creation, except some
 * of the fields are already filled in.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class EditExpenseActivity extends EditableActivity implements DatePickerFragment.TheListener{
	
	/** The category and currency. */
	private Spinner categorySpinner, currencySpinner;
	
	private TextWatcher currencyWatcher;
	
	/** The image button. */
	private ImageButton imgButton;
	
	/** The create expense button. */
	private Button cancelExpenseButton, createExpenseButton, dateButton;

	/** The description, amount. */
	private EditText description, amount;
	
	/** The status checkbox. */
	private CheckBox statusCheckBox;
	
	/** The status flag. */
	private boolean complete;
	
	/** The receipt uri. */
	private Uri receiptUri;
	
	/** The date from the DatePicker*/
	private Calendar dateSelection;
	
	/** The Claim UUID and the Expense UUID. */
	private UUID claimId, expenseId;
	
	private String curSymbol = null;
	
	private Calendar myCalendar = Calendar.getInstance();
	
	final String myFormat = "EEEE MMMM dd, yyyy"; //In which you need put here
	final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		
		final Expense newExpense = new Expense("");
		System.out.println("STARTING INIT VARS");
		initializeVariables();
		System.out.println("FINISHED INIT VARS");
		
		final Intent intent = this.getIntent();
	    final boolean isNewExpense = (boolean) intent.getBooleanExtra("isNewExpense", true);
		
	    claimId = (UUID)intent.getSerializableExtra("claimUUID");
	    expenseId = (UUID)intent.getSerializableExtra("expenseUUID");
	    final Expense expense = Controller.getExpense(EditExpenseActivity.this, claimId, expenseId);
	    final Claim claim = Controller.getClaim(this, claimId);
	    final ExpenseList newExpenseList = claim.getExpenseList();
	    
	    
	    
	    //if not a new expense, set fields to clicked expense
	    if (isNewExpense != true){
	    	description.setText(expense.getTitle().toString());
	    }

	    // The date button that shows a date dialog
		dateButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				showDatePickerDialog(v);
			}
		});
		
	    //the image button to take a picture of the receipt
	    imgButton.setOnClickListener(new Button.OnClickListener(){
	    	public void onClick(View v) {
	    		takeAPhoto();
		    }
		});
	
	    
	  //the cancel expense button
	    cancelExpenseButton.setOnClickListener(new Button.OnClickListener(){
	    	public void onClick(View v) {
	    		cancelCheck();
		    }
		});
	    
	    //the create expense button
	    createExpenseButton.setOnClickListener(new Button.OnClickListener(){
	    	public void onClick(View v) {
	    		Toast.makeText(EditExpenseActivity.this, "Updating", Toast.LENGTH_SHORT). show();
	    		if (isNewExpense == true){
	    	    	editExpense(newExpense);
	    	    	newExpenseList.addExpense(EditExpenseActivity.this, newExpense);
	    	    }else{
	    	    	editExpense(expense);
	    	    }
	    		
		    }
		});
	    
	}

	
	/**
	 * Initialize variables.
	 */
	private void initializeVariables() {
		description = (EditText) findViewById(R.id.editExpenseDescription);
		amount = (EditText) findViewById(R.id.editExpenseAmount);
		imgButton = (ImageButton) findViewById(R.id.editExpenseTakeAPhoto);
		dateButton = (Button) findViewById(R.id.tvExpenseDate);
		
		dateSelection = Calendar.getInstance();
		dateButton.setText(sdf.format(dateSelection.getTime()));
		
		categorySpinner = (Spinner) findViewById(R.id.editExpenseCategorySpinner);
		ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource 
				(this, R.array.category_array, android.R.layout.simple_spinner_item); //create array adapter using string array and default spinner layout
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //specify layout to use when list of choices appears
		categorySpinner.setAdapter(categoryAdapter);
		
		currencySpinner = (Spinner) findViewById(R.id.editExpenseCurrencySpinner);
		ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource 
				(this, R.array.currency_array, android.R.layout.simple_spinner_item); //create array adapter using string array and default spinner layout
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //specify layout to use when list of choices appears
		currencySpinner.setAdapter(currencyAdapter);
		
		/*
		final ArrayList<String> symbols = new ArrayList<String>();
		final String[] currencies = getResources().getStringArray(R.array.currency_array);
		String[] splitCurrency;
		for (int index = 0; index < currencies.length; index++){
			splitCurrency = currencies[index].split(",");
		    currencies[index] = splitCurrency[0];
		    symbols.add(splitCurrency[1]);
		}
		
		currencySpinner = (Spinner) findViewById(R.id.editExpenseCurrencySpinner);
		ArrayAdapter<String> currencyAdapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencies);
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencySpinner.setAdapter(currencyAdapter);
		
		curSymbol = symbols.get(0);
		
		currencyWatcher = new basicWatcher() {
			Boolean editting = false;

			@Override
			public void afterTextChanged(Editable s) {
				// http://stackoverflow.com/a/28865522/4269270 25/03/2015
				if (!editting) {
					editting = true;
					String sString = s.toString();
					if (sString.contains(" ")) {
						s.replace(0, sString.indexOf(" "), curSymbol);
					}
					editting = false;
				}
				
			}		
		};
		
		amount.addTextChangedListener(currencyWatcher);
		
		currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    	String amountString = amount.getText().toString();
		    	if (!amountString.isEmpty()) {
		    		curSymbol = symbols.get(pos);
		    		amount.setText(curSymbol + " " + amountString.split(" ")[1]);
		    		amount.setSelection(amount.length());
		    	}
		    }
		    public void onNothingSelected(AdapterView<?> parent) {}
		});
		*/
		statusCheckBox = (CheckBox) findViewById(R.id.editExpenseIncompleteCheckBox);
		cancelExpenseButton = (Button) findViewById(R.id.editExpenseCancelExpenseButton);
		createExpenseButton = (Button) findViewById(R.id.editExpenseCreateExpenseButton);
		
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment dateFragment = new DatePickerFragment(v, dateSelection);
	    dateFragment.show(getFragmentManager(), "datePicker");
	}
	

	@Override
	public void returnDate(View view, Calendar date) {
		dateButton.setText(sdf.format(date.getTime()));
		dateSelection = date;
    }
	
	
	/** The Constant CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE. */
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;


	/**
	 * Take a photo.
	 */
	public void takeAPhoto() {
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tracker Express";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}
		
		// Create a URI for the picture file
		String imageFilePath = folder + "/"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		File imageFile = new File(imageFilePath);
		receiptUri = Uri.fromFile(imageFile);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, receiptUri);

		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);	
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
			if (resultCode == RESULT_OK){
				Toast.makeText(EditExpenseActivity.this, "Photo Set", Toast.LENGTH_SHORT).show(); 
				Drawable photo = Drawable.createFromPath(receiptUri.getPath());
				imgButton.setImageDrawable(photo);
				//imgButton.setImageURI(receiptUri);
			} else if (resultCode == RESULT_CANCELED){
				Toast.makeText(EditExpenseActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
			} else{
				Toast.makeText(EditExpenseActivity.this, "Error", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
    
	public void editExpense(final Expense expense) {
		
		
		String title = description.getText().toString();
	
		expense.setTitle(this, title);
	
		Double money = Double.parseDouble(amount.getText().toString());
		
		expense.setAmount(this, money);
			
		
		
		String categorySelection = categorySpinner.getSelectedItem().toString();
		String currencySelection = currencySpinner.getSelectedItem().toString();
		
		BitmapDrawable photo = (BitmapDrawable) imgButton.getDrawable();
		Bitmap receipt = photo.getBitmap();
	
		if(statusCheckBox.isChecked()){
			complete = false;
		}else{
			complete = true;
		}
		
		expense.setComplete(this, complete);
		expense.setBitmap(this, receipt);
		expense.setDate(this, dateSelection);
		expense.setCategory(this, categorySelection);
		expense.setCurrency(this, currencySelection);
			
		finish();
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
    
    /**
	 * Cancelcheck.
	 */
	public void cancelCheck(){
		AlertDialog.Builder helperBuilder = new AlertDialog.Builder(EditExpenseActivity.this);
		helperBuilder.setCancelable(false);
		helperBuilder.setTitle("Warning");
		helperBuilder.setMessage("Are you sure you want to exit before saving?");
		helperBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener(){
			
			/** make Cancel button clickable
			 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
			 */
			public void onClick(DialogInterface dialog, int which){
								
				Toast.makeText(EditExpenseActivity.this, "Canceling", Toast.LENGTH_SHORT). show();
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        //do your stuff
	    	cancelCheck();
	    }
	    return super.onKeyDown(keyCode, event);
	}


}
