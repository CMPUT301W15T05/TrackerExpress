package group5.trackerexpress;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import group5.trackerexpress.EditBitmap;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
	
	private Button deleteImage;
	
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

	EditBitmap editBitmap = new EditBitmap();

	/** checks if the claim is new and cancel's appropriately */
	private boolean isNewClaim;


	/** checks if the claim has been saved and cancel's appropriately */
	private boolean isSaved;
	
	final String myFormat = "EEEE MMMM dd, yyyy"; //In which you need put here
	final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

	/** The expense in question */
	private Expense expense;
	
	/** The claim of the expense in question */
	private Claim claim;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		final Intent intent = this.getIntent();
	    claimId = (UUID)intent.getSerializableExtra("claimUUID");
	    expenseId = (UUID)intent.getSerializableExtra("expenseUUID");
	    isNewClaim = intent.getBooleanExtra("isNewClaim", false);
	    isSaved = false;
	    
	    expense = Controller.getExpense(EditExpenseActivity.this, claimId, expenseId);
	    claim = Controller.getClaim(this, claimId);
	    
		initializeVariables();
		
	    // The date button that shows a date dialog
		dateButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				showDatePickerDialog(v, Calendar.getInstance());
			}
		});
		
	    //the image button to take a picture of the receipt
	    imgButton.setOnClickListener(new Button.OnClickListener(){
	    	public void onClick(View v) {
	    		takeAPhoto();
		    }
		});
	    
	    deleteImage.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				deleteReceipt();
			}
	    	
	    });
	
	    
	  //the cancel expense button
	    cancelExpenseButton.setOnClickListener(new Button.OnClickListener(){
	    	public void onClick(View v) {
	    		cancelCheck(EditExpenseActivity.this);
	    		
	    		
		    }
		});
	    
	    //the create expense button
	    createExpenseButton.setOnClickListener(new Button.OnClickListener(){
	    	public void onClick(View v) {
	    		Toast.makeText(EditExpenseActivity.this, "Updating", Toast.LENGTH_SHORT). show();
	    	    editExpense(expense);
		    }
		});
	    
	}

	
	protected void deleteReceipt() {
		// TODO Auto-generated method stub
		receiptUri = null;
		imgButton.setImageResource(R.drawable.a);
		deleteImage.setVisibility(View.GONE);
		
	}


	/**
	 * Initialize variables.
	 */
	private void initializeVariables() {
		
		// Retrieve the views 
		description = (EditText) findViewById(R.id.editExpenseDescription);
		amount = (EditText) findViewById(R.id.editExpenseAmount);
		imgButton = (ImageButton) findViewById(R.id.editExpenseTakeAPhoto);
		dateButton = (Button) findViewById(R.id.tvExpenseDate);
		deleteImage = (Button) findViewById(R.id.deleteImageButton);
		
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
		
		statusCheckBox = (CheckBox) findViewById(R.id.editExpenseIncompleteCheckBox);
		cancelExpenseButton = (Button) findViewById(R.id.editExpenseCancelExpenseButton);
		createExpenseButton = (Button) findViewById(R.id.editExpenseCreateExpenseButton);
		
		// If expense already has values, plug them in
		if (expense.getTitle() != null){
	    	description.setText(expense.getTitle().toString());
	    }
		
	    if ( expense.getAmount() != null ){
	    	amount.setText(Double.toString(expense.getAmount()));
	    }
	    
	    if ( expense.getCurrency() != null ){
	    	currencySpinner.setSelection(getIndex(currencySpinner, expense.getCurrency()));
	    }

	    if ( expense.getCategory() != null ){
	    	categorySpinner.setSelection(getIndex(categorySpinner, expense.getCategory()));
	    }
	    
	    if ( expense.getReceipt() != null ){
			imgButton.setImageDrawable(expense.getReceipt().getDrawable());
			deleteImage.setVisibility(View.VISIBLE);
	    } else {
	    	deleteImage.setVisibility(View.GONE);
	    }
	    
	    if ( !expense.isComplete()){
	    	statusCheckBox.setChecked(true);
	    }
	    
	    statusCheckBox.setChecked(! expense.isComplete());
	    
	}
	
	@Override
	public void onStop(){
		super.onStop();
		
		if ( isNewClaim && ! isSaved ){
			claim.getExpenseList().removeExpense(EditExpenseActivity.this, expense.getUuid());
		}
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
				Bitmap sourceBitmap = ((BitmapDrawable)photo).getBitmap();
				Bitmap rotatedBitmap = editBitmap.rotateBitmap(sourceBitmap);
				Bitmap resizedBitmap = editBitmap.resizeBitmap(rotatedBitmap, 640);
				imgButton.setImageBitmap(resizedBitmap);
				deleteImage.setVisibility(View.VISIBLE);
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
	
		String amt = amount.getText().toString();
		Double money;
		if ( ParseHelper.isDoubleParsable(amt) ){
			money = Double.parseDouble(amount.getText().toString());
		} else {
			money = null;
		}
		
		expense.setAmount(this, money);
		
		String categorySelection = categorySpinner.getSelectedItem().toString();
		String currencySelection = currencySpinner.getSelectedItem().toString();
		
	
		if(statusCheckBox.isChecked()){
			complete = false;
		}else{
			complete = true;
		}
		
		if ( receiptUri != null ){
			expense.setReceipt(this, new Receipt(receiptUri.getPath()));
		}else{
			expense.setReceipt(this, null);
		}
		
		expense.setComplete(this, complete);
		expense.setDate(this, dateSelection);
		expense.setCategory(this, categorySelection);
		expense.setCurrency(this, currencySelection);
		
		isSaved = true;
		
		finish();
	}

	
	/**
	 * gets the index of a string in the spinner
	 * 
	 * @param spinner: spinner in question
	 * @param myString: the string in question
	 * @return myString's index in the spinner
	 */
	private int getIndex(Spinner spinner, String myString) {
		int index = 0;
		for (int i=0;i<spinner.getCount();i++){
			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
				index = i;
				break;
			}
		}
		return index;
	} 	
}
