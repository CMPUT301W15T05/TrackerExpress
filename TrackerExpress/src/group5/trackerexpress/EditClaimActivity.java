package group5.trackerexpress;


import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
	
	private Boolean CheckCorrectness;

	private ArrayList<String[]> Destination;
	private ArrayAdapter<String> adapter2;
	
	private final int newDestination = 1;
	private final int editDestination = 2;
	private final int doNothing = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		
		Destination = new ArrayList<String[]>();
		
		final Claim newclaim = new Claim("");
		
		ClaimName = (EditText) findViewById(R.id.editClaimName);
		ClaimTitle = (EditText) findViewById(R.id.editClaimTitle);
		
		StartDateYear = (EditText) findViewById(R.id.editClaimStartDateYear);
		StartDateMonth = (EditText) findViewById(R.id.editClaimStartDateMonth);
		StartDateDay = (EditText) findViewById(R.id.editClaimStartDateDay);

		EndDateYear = (EditText) findViewById(R.id.editClaimEndDateYear);
		EndDateMonth = (EditText) findViewById(R.id.editClaimEndDateMonth);
		EndDateDay = (EditText) findViewById(R.id.editClaimEndDateDay);
		Description = (EditText) findViewById(R.id.editClaimDescription);
		
		ListView myListView = (ListView) findViewById(R.id.listViewDestinations);
		
		
		final Intent intent = this.getIntent();
	    final boolean isNewClaim = (boolean) intent.getBooleanExtra("isNewClaim", true);
	    final ClaimList newclaimlist = ClaimController.getInstance(EditClaimActivity.this).getClaimList();
	    
	    UUID serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
	    final Claim claim = ClaimController.getInstance(EditClaimActivity.this).getClaimList().getClaim(serialisedId);
	    
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
	    
		
		
		/* create a dummy 2d destination array once the create_claim_button is clicked, 
		 * it will save this array into the claim.(use getDestination(this, ArrayList<String[]> yourDestination*/
		
		
		
		
	    Button editDestinationButton = (Button) findViewById(R.id.buttonAddDestination);
		
		editDestinationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isNewClaim == true){
					createDestinationButton(isNewClaim,Destination,newDestination,doNothing);
				} else {
					Destination = claim.getDestination();
					createDestinationButton(isNewClaim, Destination,newDestination,doNothing);
				}
				
			}
		});
		
		Button done = (Button) findViewById(R.id.buttonCreateClaim);
		
	    if (isNewClaim == true){
			    	done.setText("Create Claim");
			    	
			    	DestinationListview(myListView,Destination);
			    	
			    	
			    } else {
			    	
			    	done.setText("Edit Claim");
			    	Destination = claim.getDestination();
				    ClaimName.setText(claim.getuserName());
					ClaimTitle.setText(claim.getClaimName());
					
					StartDateYear.setText(String.valueOf(claim.getStartDate().getYYYY()));
					StartDateMonth.setText(String.valueOf(claim.getStartDate().getMM()));
					StartDateDay.setText(String.valueOf(claim.getStartDate().getDD()));
					
					EndDateYear.setText(String.valueOf(claim.getEndDate().getYYYY()));
					EndDateMonth.setText(String.valueOf(claim.getEndDate().getMM()));
					EndDateDay.setText(String.valueOf(claim.getEndDate().getDD()));
					
					Description.setText(String.valueOf(claim.getDescription()));
					DestinationListview(myListView,Destination);
				    
			    }
	    
	    myListView.setOnItemClickListener(onListClick);
	    
	    
	    
	    done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (isNewClaim == true){
					editclaim(newclaim);
					newclaimlist.addClaim(EditClaimActivity.this, newclaim);
					newclaim.setDestination(EditClaimActivity.this, Destination);
					
				} else{
					editclaim(claim);
					claim.setDestination(EditClaimActivity.this, Destination);
				}
				
				
				/* this statement checks if the text field is valid or not.*/
				
				if( ClaimName.getText().toString().length() == 0 ){
				    ClaimName.setError( "Name is required!" );
				}
				
			    Toast.makeText(EditClaimActivity.this, "Updating", Toast.LENGTH_SHORT). show();
				
				// launch CreateNewClaimActivity.
				Intent intent = new Intent(EditClaimActivity.this, MainActivity.class);
		    	startActivity(intent);
			}
		});
	    
	    Button cancel = (Button) findViewById(R.id.button_cancel_edit_claim);
	    cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder helperBuilder = new AlertDialog.Builder(EditClaimActivity.this);
				helperBuilder.setCancelable(false);
				helperBuilder.setTitle("Warning");
				helperBuilder.setMessage("Are you sure you want to exit before saving?");
				helperBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						
						Toast.makeText(EditClaimActivity.this, "Canceling", Toast.LENGTH_SHORT). show();
						
						// launch CreateNewClaimActivity.
						Intent intent = new Intent(EditClaimActivity.this, MainActivity.class);
				    	startActivity(intent);
					}
				});
				
				helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				
					@Override
					public void onClick(DialogInterface dialog, int which){
						
					}
				});
				AlertDialog helpDialog = helperBuilder.create();
				helpDialog.show();
			}
		});
	}
	
	@Override
	public void onStop(){
		super.onStop();
		finish();
	}
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			// TODO Auto-generated method stub
			// http://www.androiddom.com/2011/06/displaying-android-pop-up-dialog_13.html 	2015-03-11
			AlertDialog.Builder helperBuilder = new AlertDialog.Builder(EditClaimActivity.this);
			
			helperBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					createDestinationButton(false, Destination,editDestination,position);
				}
			});
			
			helperBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {

				  @Override
				  public void onClick(DialogInterface dialog, int which) {
				   // Do nothing
				  }
				 });
			
			helperBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener(){
			
				@Override
				public void onClick(DialogInterface dialog, int which){
					String toRemove = adapter2.getItem(position);
					adapter2.remove(toRemove);
					adapter2.notifyDataSetChanged();
				}
			});
			AlertDialog helpDialog = helperBuilder.create();
			helpDialog.show();
		}
	};
	
	private void editclaim(final Claim claim) {
		// TODO Auto-generated method stub
		
		
		String claimUser = ClaimName.getText().toString();
		String Claim_title = ClaimTitle.getText().toString();
		
		String SDateY = StartDateYear.getText().toString();
		if (ParseHelper.isIntegerParsable(SDateY)){
			
		}
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
		
		
	}




	private void createDestinationButton( final boolean isNewClaim, final ArrayList<String[]> destination2, final int i,final int position) {
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
        
				
				
		switch(i){
				
		case newDestination:
			helperBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					String Des_Name = DesName.getText().toString();
					String Des_Rea = DesRea.getText().toString();
					editDummyDestination(EditClaimActivity.this, Des_Name, Des_Rea, doNothing, null, newDestination);
				}
			});
			
			helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
							
				}
			});
					
			AlertDialog helperDialog = helperBuilder.create();
			helperDialog.show();
			break;
					
		case editDestination:
			DesName.setText(destination2.get(position)[0]);
			DesRea.setText(destination2.get(position)[1]);
			final String oldDestination = destination2.get(position)[0]+" - "+destination2.get(position)[1];
			
			helperBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					String Des_Name2 = DesName.getText().toString();
					String Des_Rea2 = DesRea.getText().toString();
					editDummyDestination(EditClaimActivity.this, Des_Name2, Des_Rea2, position, oldDestination,editDestination);
				}
			});
			
			
			helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
							
				}
			});
					
			AlertDialog helpDialog = helperBuilder.create();
			helpDialog.show();
			break;
		}
				
				
	}
	public void editDummyDestination(Context context, String place, String Reason, int position, String oldDestination, int i){
		String[] travelInfo = new String[2];
		travelInfo[0] = place;
		travelInfo[1] = Reason; 
		switch(i){
		case newDestination:
			adapter2.add(place + " - " + Reason);
			adapter2.notifyDataSetChanged();
			Destination.add(travelInfo);
			break;
		case editDestination:
			adapter2.insert(place+ " - " + Reason, position);
			adapter2.remove(oldDestination);
			Destination.set(position,travelInfo);
			adapter2.notifyDataSetChanged();
		}	
	}
	
	public void DestinationListview(ListView myListView, ArrayList<String[]> destination){
		
		ArrayList<String> destinationArray = destinationReason(destination);
		adapter2 = new ArrayAdapter<String>(this,  
		          R.layout.edit_claim_listview, 
		          destinationArray);
		myListView.setAdapter(adapter2);
	}
	
	public ArrayList<String> destinationReason(ArrayList<String[]> destination2){
		final ArrayList<String> destinationreason = new ArrayList<String>();
		String destination_reason = "";
		for (int i = 0; i< destination2.size(); i++){
			destination_reason = destination2.get(i)[0]+ " - " + destination2.get(i)[1];
			destinationreason.add(destination_reason);
		}
		return destinationreason;
	}
	
	
	public void usercorrectness(String string){
		
		if (string == null){
			
		}
		
	}
	
	
}
