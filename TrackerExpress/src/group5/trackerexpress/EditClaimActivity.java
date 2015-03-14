package group5.trackerexpress;


import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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
	private EditText StartDateYear;
	private EditText StartDateMonth;
	private EditText StartDateDay;
	private EditText EndDateYear;
	private EditText EndDateMonth;
	private EditText EndDateDay;
	private EditText Description; 
	private EditText DesName;
	private EditText DesRea;

	private ArrayList<String[]> Destination;
	private ArrayAdapter<String[]>adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		
		
		ClaimName = (EditText) findViewById(R.id.editClaimName);
		ClaimTitle = (EditText) findViewById(R.id.editClaimTitle);
		
		StartDateYear = (EditText) findViewById(R.id.editClaimStartDateYear);
		StartDateMonth = (EditText) findViewById(R.id.editClaimStartDateMonth);
		StartDateDay = (EditText) findViewById(R.id.editClaimStartDateDay);

		EndDateYear = (EditText) findViewById(R.id.editClaimEndDateYear);
		EndDateMonth = (EditText) findViewById(R.id.editClaimEndDateMonth);
		EndDateDay = (EditText) findViewById(R.id.editClaimEndDateDay);
		Description = (EditText) findViewById(R.id.editClaimDescription);
		
		
		Intent intent = this.getIntent();
	    boolean isNewClaim = (boolean) intent.getBooleanExtra("isNewClaim", true);

		System.err.println ("~/~ NEW CLAIM ~/~");
	    if (isNewClaim == true){
			System.err.println ("~/~ NEW CLAIM ~/~");
	    	newClaimcreate();
	    } else {
		    UUID serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
		    final Claim claim = ClaimController.getInstance(this).getClaimList().getClaim(serialisedId);
		    editExistingclaim(claim);
	    }
		
	}
	
	
	private void editExistingclaim(Claim claim) {
		// TODO Auto-generated method stub
		
		ClaimName.setText(claim.getuserName());
		ClaimTitle.setText(claim.getClaimName());
		StartDateYear.setText(claim.getStartDate().getYYYY());
		StartDateMonth.setText(claim.getStartDate().getMM());
		StartDateDay.setText(claim.getStartDate().getDD());
		EndDateYear.setText(claim.getEndDate().getYYYY());
		EndDateYear.setText(claim.getEndDate().getMM());
		EndDateYear.setText(claim.getEndDate().getDD());
		Description.setText(claim.getDescription());
		
		String claimUser = ClaimName.getText().toString();
		String Claim_title = ClaimTitle.getText().toString();
		
		String SDateY = StartDateYear.getText().toString();
		int mySDateY = Integer.parseInt(SDateY);
		
		String SDateM = StartDateMonth.getText().toString();
		int mySDateM = Integer.parseInt(SDateM);
		
		String SDateD = StartDateDay.getText().toString();
		int mySDateD = Integer.parseInt(SDateD);
		
		String EDateY = EndDateYear.getText().toString();
		int myEDateY = Integer.parseInt(EDateY);
		
		String EDateM = EndDateMonth.getText().toString();
		int myEDateM = Integer.parseInt(EDateM);
		
		String EDateD = EndDateDay.getText().toString();
		int myEDateD = Integer.parseInt(EDateD);
		
		String Descrip = Description.getText().toString();
		
		
		claim.setuserName(this, claimUser);
		claim.setClaimName(this, Claim_title);
		
		Date d1 = new Date(mySDateY, mySDateM, mySDateD);
		Date d2 = new Date(myEDateY, myEDateM, myEDateD);
		
		claim.setStartDate(this, d1);
		claim.setEndDate(this, d2);
		claim.setDescription(this, Descrip);
		
		
		Button editDestinationButton = (Button) findViewById(R.id.buttonAddDestination);
		
		editDestinationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createDestinationButton();
				
			}
		});
		
		tagClaimButton();
		
	}


	private void newClaimcreate() {
		// TODO Auto-generated method stub
		/* create a new claim object and use the controller 
		to get access to the claimList and add the new claim to claimList.*/
		Claim newclaim = new Claim(null);
		final ClaimList claimlist = ClaimController.getInstance(this).getClaimList();
		
		
		String claimUser = ClaimName.getText().toString();
		String Claim_title = ClaimTitle.getText().toString();
		
		String SDateY = StartDateYear.getText().toString();
		int mySDateY = Integer.parseInt(SDateY);
		
		String SDateM = StartDateMonth.getText().toString();
		int mySDateM = Integer.parseInt(SDateM);
		
		String SDateD = StartDateDay.getText().toString();
		int mySDateD = Integer.parseInt(SDateD);
		
		String EDateY = EndDateYear.getText().toString();
		int myEDateY = Integer.parseInt(EDateY);
		
		String EDateM = EndDateMonth.getText().toString();
		int myEDateM = Integer.parseInt(EDateM);
		
		String EDateD = EndDateDay.getText().toString();
		int myEDateD = Integer.parseInt(EDateD);
		
		String Descrip = Description.getText().toString();
		
		newclaim.setuserName(this, claimUser);
		newclaim.setClaimName(this, Claim_title);
		
		Date d1 = new Date(mySDateY, mySDateM, mySDateD);
		Date d2 = new Date(myEDateY, myEDateM, myEDateD);
		
		newclaim.setStartDate(this, d1);
		newclaim.setEndDate(this, d2);
		newclaim.setDescription(this, Descrip);
		
		
		
		Button editDestinationButton = (Button) findViewById(R.id.buttonAddDestination);
		
		editDestinationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createDestinationButton();
				
			}
		});
		
		tagClaimButton();
		claimlist.addClaim(this, newclaim);
	}


	private void createDestinationButton() {
		// TODO Auto-generated method stub
		// http://www.androiddom.com/2011/06/displaying-android-pop-up-dialog_13.html 	2015-03-11
		AlertDialog.Builder helperBuilder = new AlertDialog.Builder(this);
		helperBuilder.setCancelable(false);
		helperBuilder.setTitle("Destinations");
		
		LayoutInflater inflater = getLayoutInflater();
        View popupview = inflater.inflate(R.layout.activity_popup_destination, null);
        helperBuilder.setView(popupview);
        
        DesName = (EditText) popupview.findViewById(R.id.inputDestination);
        DesRea = (EditText) popupview.findViewById(R.id.inputDestinationReason);
        
		helperBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					
			public void onClick(DialogInterface dialog, int which) {
				String Des_Name = DesName.getText().toString();
				String Des_Rea = DesRea.getText().toString();
				
				
						
			}
		});
				
		helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
			@Override
			public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
						
			}
		});
				
		AlertDialog helpDialog = helperBuilder.create();
		helpDialog.show();
				
				
	}
				


	private void tagClaimButton(){
		
		Button addTagsButton= (Button) findViewById(R.id.buttonEditTags);
		
		addTagsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// display message "Creating new claim".
				Toast.makeText(EditClaimActivity.this, "Loading", Toast.LENGTH_SHORT). show();
				
				// launch CreateNewClaimActivity.
				Intent intent = new Intent(EditClaimActivity.this, SelectTag_claim_Activity.class);
		    	startActivity(intent);
			}
		});
	}


}
