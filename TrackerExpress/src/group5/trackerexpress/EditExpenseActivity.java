package group5.trackerexpress;

import java.io.File;

import android.app.Activity;
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

public class EditExpenseActivity extends Activity {
	
	private Spinner category, currency;
	private ImageButton imgButton;
	private Button createExpenseButton;
	private CheckBox flagCheckBox;
	private EditText description, amount;
	private Uri receiptUri;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		OnClickListener picListener = new OnClickListener() {
			public void onClick(View v) {
				takeAPhoto();
			}
		};
		imgButton.setOnClickListener(picListener);
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

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
	
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
		parent.getItemAtPosition(position);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}
