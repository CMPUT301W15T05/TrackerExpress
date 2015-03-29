package group5.trackerexpress;


import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class EditExpenseActivity.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class EditExpenseActivity extends EditableActivity implements DatePickerFragment.TheListener{
	
	/** The category and currency. */
	private Spinner categorySpinner, currencySpinner;
	
	/** The img button. */
	private ImageButton imgButton;
	
	/** The create expense button. */
	private Button createExpenseButton, dateButton;

	/** The description, amount. */
	private EditText description, amount;
	
	/** The status checkbox. */
	private CheckBox flagCheckBox;
	
	/** The status flag. */
	private int flagStatus;
	
	/** The receipt uri. */
	private Uri receiptUri;
	
	/** The date from the DatePicker*/
	private Date dateSelection;
	
	private String curSymbol = null;

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
		
	    UUID claimId = (UUID)intent.getSerializableExtra("claimUUID");
	    UUID expenseId = (UUID)intent.getSerializableExtra("expenseUUID");
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
	   
	    //the create expense button
	    createExpenseButton.setOnClickListener(new Button.OnClickListener(){
	    	public void onClick(View v) {
	    		if (isNewExpense == true){
	    	    	editExpense(newExpense);
	    	    	newExpenseList.addExpense(EditExpenseActivity.this, expense);
	    	    }else{
	    	    	editExpense(expense);
	    	    } 
	    		
	    		finish();
	    		
		    }
		});
	  
	}
	
	private TextWatcher currencyWatcher = new basicWatcher() {
		Boolean editting = false;
		String preChange = "";

		@Override 
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			preChange = s.toString();
		};
		
		@Override
		public void afterTextChanged(Editable s) {
			String sString = s.toString();
			
			// http://stackoverflow.com/a/28865522/4269270 25/03/2015
			if (!editting && !sString.isEmpty()) {
				editting = true;
				
				if (preChange.equals("0.") && sString.equals("0")) {
					s.delete(0, 1);
					editting = false;
					return;
				}
				
				if (sString.contains(".")) {
					Integer pointIndex = sString.indexOf(".");
					
					// x.xxx -> x.xx
					if (sString.length() > pointIndex + 3) {
						s.delete(pointIndex + 3, s.length());
					}
					
					// .xx -> 0.xx
					if (pointIndex.equals(0)) {
						s.insert(0, "0");
					}
				}
				
				
				// 0x.x -> x.x
				while (s.charAt(0) == '0') {
					if (s.length() < 2 || s.charAt(1) == '.') {
						break;
					}
				
					s.delete(0, 1);
				}
				
				editting = false;
			}
		}
	};
	
	/**
	 * Initialize variables.
	 */
	private void initializeVariables() {
		description = (EditText) findViewById(R.id.editExpenseDescription);
		
		amount = (EditText) findViewById(R.id.editExpenseAmount);
		amount.addTextChangedListener(currencyWatcher);
		
		imgButton = (ImageButton) findViewById(R.id.editExpenseTakeAPhoto);
		dateButton = (Button) findViewById(R.id.tvExpenseDate);
		
		dateSelection = new Date();
		dateButton.setText(dateSelection.getLongString());
		
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
		
		createExpenseButton = (Button) findViewById(R.id.editExpenseCreateExpenseButton);
		flagCheckBox = (CheckBox) findViewById(R.id.editExpenseIncompleteCheckBox);
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment dateFragment = new DatePickerFragment(dateSelection);
	    dateFragment.show(getFragmentManager(), "datePicker");
	}
	
	@Override
	public void returnDate(Date date) {
		dateButton.setText(date.getLongString());
		dateSelection = date;
    }
	
	
	/** The Constant CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE. */
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;


	/**
	 * Take a photo.
	 */
	public void takeAPhoto() {
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
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
	
	public void status(View v) {
        //code to check if this checkbox is checked!
		flagCheckBox = (CheckBox)v;
        if(flagCheckBox.isChecked()){
        	flagStatus = 1;
        }else{
        	flagStatus = 0;
        }
    }

    
	private void editExpense(final Expense expense) {
		
		
		String title = description.getText().toString();
		String sAmount = amount.getText().toString();
		Double money;
		
		if (!sAmount.isEmpty()) {
			money = Double.parseDouble(sAmount);
		} else {
			money = 0.0;
		}
	
		String categorySelection = categorySpinner.getSelectedItem().toString();
		String currencySelection = currencySpinner.getSelectedItem().toString();
		
		expense.setTitle(this, title);
		expense.setAmount(this, money);
		expense.setDate(this, dateSelection);
		expense.setStatus(this, flagStatus);
		expense.setCategory(this, categorySelection);
		expense.setCurrency(this, currencySelection);
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
