package group5.trackerexpress;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

// TODO: Auto-generated Javadoc
/**
 * The Class EditExpenseActivity.
 */
public class EditExpenseActivity extends Activity {
	
	/** The currency. */
	private Spinner category, currency;
	
	/** The img button. */
	private ImageButton imgButton;
	
	/** The create expense button. */
	private Button createExpenseButton;
	
	/** The flag check box. */
	private CheckBox flagCheckBox;
	
	/** The description, amount and date. */
	private EditText description, amount, date;
	
	/** The receipt uri. */
	private Uri receiptUri;
	
	/** The intent. */
	final Intent intent = this.getIntent();
	
	/** The serialised id. */
	UUID serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
	
	/** The claim. */
	final Claim claim = ClaimController.getInstance(EditExpenseActivity.this).getClaimList().getClaim(serialisedId);
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		final Expense newExpense = new Expense("");
		
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
	 * Initialize vars.
	 */
	private void initializeVars() {
		// TODO Auto-generated method stub
		description = (EditText) findViewById(R.id.editDescription);
		amount = (EditText) findViewById(R.id.editAmount);
		imgButton = (ImageButton) findViewById(R.id.TakeAPhoto);
		date = (EditText) findViewById(R.id.editDate);
		
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
		Date d1 = null;
		
		String title = description.getText().toString();
		expense.setTitle(this, title);
		
		Double money = Double.parseDouble(amount.getText().toString());
		expense.setAmount(this, money);
		
		String categorySelection = category.getSelectedItem().toString();
		expense.setCategory(this, categorySelection);
		
		String currencySelection = currency.getSelectedItem().toString();
		expense.setCurrency(this, currencySelection);
		
		String expenseDate = date.getText().toString();
		expense.setDate(this, d1);
		
		
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
