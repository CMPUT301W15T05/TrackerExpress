package group5.trackerexpress;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

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
	private EditText TagName;
	
	private ListView desListView;
	private ListView tagListView;
	
	private Editable value;
	
	private final HashSet<Tag> tagsOfClaim = new HashSet<Tag>();
	
	private Boolean checkCorrectness;

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
		
		desListView = (ListView) findViewById(R.id.listViewDestinations);
		tagListView = (ListView) findViewById(R.id.listViewTagsEditClaim);
		tagListView.setItemsCanFocus(true);
		
		/*****************************
		 * ADDED CODE START
		 *****************************/
				
		Button b_add_tag = (Button) findViewById(R.id.buttonEditTags);
		
		b_add_tag.setOnClickListener(new Button.OnClickListener(){
		    public void onClick(View v) {
		    	getAndSetTag();
		    }
		});
		
		// Tag List Item click listener
		tagListView.setOnItemClickListener( new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent,
					View view, final int position, long id) {
				
				PopupMenu popup = new PopupMenu(EditClaimActivity.this, view);
				popup.getMenuInflater().inflate(R.menu.edit_claim_tag_list_popup, popup.getMenu());
				
				// Popup menu item click listener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                    	Tag t = (Tag) tagListView.getAdapter().getItem(position);
                    	
                        switch(item.getItemId()){
                        case R.id.op_edit_claim_delete_tag: 
                        	tagsOfClaim.remove(t);
                        	updateTagListView(new ArrayList<Tag>(tagsOfClaim));
                        	break;
                        	default: break;
                        }
                    	
                        return true;
                    }
                });
				
	            popup.show();
			}
        });
		
		
		/*****************************
		 * ADDED CODE END
		 *****************************/
		
		final Intent intent = this.getIntent();
	    final boolean isNewClaim = (boolean) intent.getBooleanExtra("isNewClaim", true);
	    final ClaimList newclaimlist = ClaimController.getInstance(EditClaimActivity.this).getClaimList();
	    
	    UUID serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
	    final Claim claim = ClaimController.getInstance(EditClaimActivity.this).getClaimList().getClaim(serialisedId);
	    
	    Button addTagsButton= (Button) findViewById(R.id.buttonEditTags);
		
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
		    DestinationListview(desListView,Destination);
		   		
	    } else {
		   	done.setText("Edit Claim");
		   	Destination = claim.getDestination();
		    ClaimName.setText(claim.getuserName());
			ClaimTitle.setText(claim.getClaimName());
					
			if ( claim.getStartDate() != null ){
				StartDateYear.setText(String.valueOf(claim.getStartDate().getYYYY()));
				StartDateMonth.setText(String.valueOf(claim.getStartDate().getMM()));
				StartDateDay.setText(String.valueOf(claim.getStartDate().getDD()));
			}
					
			if ( claim.getStartDate() != null ){
				EndDateYear.setText(String.valueOf(claim.getEndDate().getYYYY()));
				EndDateMonth.setText(String.valueOf(claim.getEndDate().getMM()));
				EndDateDay.setText(String.valueOf(claim.getEndDate().getDD()));
			}
			Description.setText(String.valueOf(claim.getDescription()));
			DestinationListview(desListView,Destination);
			
			/* Saving new tags */
			ArrayList<Tag> current = TagController.getInstance(this).getTagMap().getTags();
			for ( Tag t : tagsOfClaim ){
				if ( ! current.contains(t) ){
					TagController.getInstance(this).getTagMap().addTag(this, t);
				}
			}
	    }
	    
	    desListView.setOnItemClickListener(onListClick);
	    
	    done.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				
				/* this procedure will check if the claim name is repeated */
				boolean repeatedClaimName = false;
				Claim[] claims = ClaimController.getInstance(EditClaimActivity.this).getClaimList().getAllClaims();
				for ( Claim c : claims ){
						if ( c.getClaimName().equals( ClaimTitle.getText().toString() )
							&& ( isNewClaim || ! c.getUuid().equals(claim.getUuid())) ){
						repeatedClaimName = true;
					}
				}
				
				/* this statement checks if the text fields are valid or not.*/
				if( ClaimName.getText().toString().length() == 0 || ClaimTitle.getText().toString().length() == 0 ){
				    if ( ClaimName.getText().toString().length() == 0 ){
				    	ClaimName.setError( "Name is required!" );
				    }
				    if ( ClaimTitle.getText().toString().length() == 0 ){
				    	ClaimTitle.setError( "Title is required!" );
				    }
				} else if (repeatedClaimName) {
					ClaimTitle.setError( "Repeated claim name!" );
				} else {
				
				
					Toast.makeText(EditClaimActivity.this, "Updating", Toast.LENGTH_SHORT). show();
					
					if (isNewClaim == true){
						editclaim(newclaim);
						newclaimlist.addClaim(EditClaimActivity.this, newclaim);
						newclaim.setDestination(EditClaimActivity.this, Destination);
					} else{
						editclaim(claim);
						claim.setDestination(EditClaimActivity.this, Destination);
					}
					
					// launch CreateNewClaimActivity.
					Intent intent = new Intent(EditClaimActivity.this, MainActivity.class);
					startActivity(intent);
				}
			}
		});
	    
	    Button cancel = (Button) findViewById(R.id.button_cancel_edit_claim);
	    cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(EditClaimActivity.this, "Canceling", Toast.LENGTH_SHORT). show();
				
				// launch CreateNewClaimActivity.
				Intent intent = new Intent(EditClaimActivity.this, MainActivity.class);
		    	startActivity(intent);
			}
		});
	    
	}
	
	@Override
	public void onStop(){
		super.onStop();
		finish();
	}
	
	/* displays a popup autocomplete text view so the user can enter
	 * a tag name, and then updates the list view
	 */
	private void getAndSetTag(){
		String message = "Enter a new name";
		final AutoCompleteTextView input = new AutoCompleteTextView(EditClaimActivity.this);

		ArrayList<Tag> tagList = TagController.getInstance(EditClaimActivity.this).getTagMap().getTags();
		ArrayList<String> tags = new ArrayList<String>();
		
		for ( int i = 0; i < tagList.size(); i++ ){
			tags.add(tagList.get(i).toString());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditClaimActivity.this, R.layout.edit_claim_drop_down_item, tags);
		input.setAdapter(adapter);
		input.setThreshold(1);
		input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			  @Override
			  public void onFocusChange(View view, boolean hasFocus) {
				  if(hasFocus){
					  input.showDropDown();
				  }
			  }
		});
		
		new AlertDialog.Builder(this)
	    .setTitle("Create Tag")
	    .setMessage(message)
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {       		
	        	value = input.getText();
		    	if ( input.getText() != null ){
		    		Tag newTag = new Tag(input.getText().toString());
		    		boolean notInSet = true;
		    		for ( Tag t : tagsOfClaim ){
		    			if ( t.toString().equals(newTag.toString()) ){
		    				notInSet = false;
		    			}
		    		}
		    		if ( notInSet ){
		    			tagsOfClaim.add(newTag);
		    		}
		    		
		    		updateTagListView(new ArrayList<Tag>(tagsOfClaim));
		    	}
		    	
		    	value = null;
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            // Do nothing.
	        }
	    }).show();
		
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
			
			helperBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

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
					Destination.remove(position);
					adapter2.notifyDataSetChanged();
				}
			});
			AlertDialog helpDialog = helperBuilder.create();
			helpDialog.show();
		}
	};
	
	private void editclaim(final Claim claim) {
		// TODO Auto-generated method stub
		int mySDateY, mySDateM, mySDateD,myEDateY, myEDateM, myEDateD;
		Date d2 = null;
		Date d1 = null;
		String claimUser = ClaimName.getText().toString();
		String Claim_title = ClaimTitle.getText().toString();
		
		String SDateY = StartDateYear.getText().toString();
		String SDateM = StartDateMonth.getText().toString();
		String SDateD = StartDateDay.getText().toString();
		
		String EDateY = EndDateYear.getText().toString();
		String EDateM = EndDateMonth.getText().toString();
		String EDateD = EndDateDay.getText().toString();
		
		String Descrip = Description.getText().toString();
		
		if (ParseHelper.isIntegerParsable(EDateD) && 
			ParseHelper.isIntegerParsable(EDateM) && 
			ParseHelper.isIntegerParsable(EDateY)){
			
			myEDateD = Integer.parseInt(EDateD);
			myEDateM = Integer.parseInt(EDateM);
			myEDateY = Integer.parseInt(EDateY);
			d2 = new Date(myEDateY, myEDateM, myEDateD);
		}
		
		if (ParseHelper.isIntegerParsable(SDateD) &&
			ParseHelper.isIntegerParsable(SDateM) &&
			ParseHelper.isIntegerParsable(SDateY)){
			
			mySDateD = Integer.parseInt(SDateD);
			mySDateM = Integer.parseInt(SDateM);
			mySDateY = Integer.parseInt(SDateY);
			d1 = new Date(mySDateY, mySDateM, mySDateD);
		}
		
		claim.setuserName(this, claimUser);
		claim.setClaimName(this, Claim_title);

		claim.setStartDate(this, d1);
		claim.setEndDate(this, d2);
		claim.setDescription(this, Descrip);
		
		ClaimController.getInstance(this).getClaimList().addClaim(this, claim);		
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
	
	/* updates tag list view */
	private void updateTagListView( ArrayList<Tag> tagList ){
		if ( tagList == null ){
			tagList = TagController.getInstance(this).getTagMap().getTags();
		}
		MainTagListAdapter adapter = new MainTagListAdapter(this, tagList);
		tagListView.setAdapter(adapter);
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
	
}
