package group5.trackerexpress;

import java.io.File;
import java.util.UUID;

import android.app.DialogFragment;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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
	private Button createExpenseButton;
	
	/** The flag check box. */
	private CheckBox flagCheckBox;
	
	/** The description, amount. */
	private EditText description, amount;
	
	/**The date. */
	private TextView dateTextView;
	
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
		initializeVariables();
		System.out.println("FINISHED INIT VARS");
		
		final Intent intent = this.getIntent();
	    final boolean isNewExpense = (boolean) intent.getBooleanExtra("isNewExpense", true);
		
	    UUID serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
	    final Claim claim = Controller.getClaim(EditExpenseActivity.this, serialisedId);
	    
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
			
		OnClickListener finishListener = new OnClickListener() {
			public void onClick(View v) {
				editExpense(newExpense);
			}
		};
		createExpenseButton.setOnClickListener(finishListener);
	}
	
	/**
	 * Initialize variables.
	 */
	private void initializeVariables() {
		intent = this.getIntent();
		serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
		claim = Controller.getClaimList(EditExpenseActivity.this).getClaim(serialisedId);
		
		description = (EditText) findViewById(R.id.editExpenseDescription);
		amount = (EditText) findViewById(R.id.editExpenseAmount);
		imgButton = (ImageButton) findViewById(R.id.editExpenseTakeAPhoto);
		dateTextView = (TextView) findViewById(R.id.tvExpenseDate);
		
		categorySpinner = (Spinner) findViewById(R.id.editExpenseCategorySpinner);
		ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource 
				(this,  R.array.category_array,  android.R.layout.simple_spinner_item); //create array adapter using string array and default spinner layout
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //specify layout to use when list of choices appears
		categorySpinner.setAdapter(categoryAdapter);
		
		currencySpinner = (Spinner) findViewById(R.id.editExpenseCurrencySpinner);
		ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource
				(this,  R.array.currency_array,  android.R.layout.simple_spinner_item); //create array adapter using string array and default spinner layout
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //specify layout to use when list of choices appears
		currencySpinner.setAdapter(currencyAdapter);
		
		createExpenseButton = (Button) findViewById(R.id.editExpenseCreateExpenseButton);
		flagCheckBox = (CheckBox) findViewById(R.id.editExpenseIncompleteCheckBox);
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment dateFragment = new DatePickerFragment();
	    dateFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void returnDate(String date) {
        // TODO Auto-generated method stub
		dateTextView.setText(date);
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

		ImageButton button = (ImageButton) findViewById(R.id.editExpenseTakeAPhoto);
		Drawable photo = Drawable.createFromPath(receiptUri.getPath());
		button.setImageDrawable(photo);
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
	
	



    
	private void editExpense(final Expense expense) {
		
		String title = description.getText().toString();
		expense.setTitle(this, title);
		
		Double money = Double.parseDouble(amount.getText().toString());
		expense.setAmount(this, money);
		
		String categorySelection = categorySpinner.getSelectedItem().toString();
		expense.setCategory(this, categorySelection);
		
		String currencySelection = currencySpinner.getSelectedItem().toString();
		expense.setCurrency(this, currencySelection);
		
		Date dateSelection = (Date) dateTextView.getText();
		expense.setDate(this, dateSelection);
		
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
