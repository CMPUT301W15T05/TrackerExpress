package group5.trackerexpress;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class EditClaimActivity extends Activity {
	
	private EditText ClaimName;
	private EditText ClaimTitle;
	private EditText StartDate;
	private EditText EndDate;
	private EditText Description; 
	private ArrayList<String[]> Destination;
	private ArrayAdapter<String[]>aapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		
		ClaimName = (EditText) findViewById(R.id.editClaimName);
		ClaimTitle = (EditText) findViewById(R.id.editClaimTitle);
		StartDate = (EditText) findViewById(R.id.editClaimStartDate);
		EndDate = (EditText) findViewById(R.id.editClaimEndDate);
		Description = (EditText) findViewById(R.id.editClaimDescription);
		
		
		String claimUser = ClaimName.getText().toString();
		String Claim_title = ClaimTitle.getText().toString();
		String SDate = StartDate.getText().toString();
		String EDate = EndDate.getText().toString();
		String Descrip = Description.getText().toString();
		
		
		createDestinationButton();
		tagCalimButton();
		
		
		
	}
	
	
	private void tagCalimButton() {
		// TODO Auto-generated method stub
		
		final Button addTagsButton = (Button) findViewById(R.id.buttonEditTags);
		
		addTagsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				LayoutInflater inflator = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				
				View popupview = inflator.inflate(R.layout.activity_popup_destination,null);
				
				final PopupWindow popupWindow = new PopupWindow(popupview,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				
				Button doneDestination  = (Button)popupview.findViewById(R.id.button_done_destination);
				doneDestination.setOnClickListener(new View.OnClickListener(){
					
					
					public void onClick(View v){
						popupWindow.dismiss();
					}
				});
				popupWindow.showAsDropDown(addTagsButton,50,-30);
			}
		});
	}
				


	private void createDestinationButton(){
		
		Button editDestinationButton = (Button) findViewById(R.id.buttonAddDestination);
		
		editDestinationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// display message "Creating new claim".
				Toast.makeText(EditClaimActivity.this, "Loading", Toast.LENGTH_SHORT). show();
				
				// launch CreateNewClaimActivity.
				Intent intent = new Intent(EditClaimActivity.this, DestinationActivity.class);
		    	startActivity(intent);
			}
		});
	}


}
