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
import android.view.KeyEvent;
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

// TODO: Auto-generated Javadoc
/**
 * The Class EditClaimActivity.
 */
/**
 * @author RandyHu
 *
 */
public class EditClaimActivity extends Activity {
	

	/** The Claim name. */
	private EditText ClaimName;
	
	/** The Claim title. */
	private EditText ClaimTitle;
	
	/** The Start date year. */
	private EditText StartDateYear;
	
	/** The Start date month. */
	private EditText StartDateMonth;
	
	/** The Start date day. */
	private EditText StartDateDay;
	
	/** The End date year. */
	private EditText EndDateYear;
	
	/** The End date month. */
	private EditText EndDateMonth;
	
	/** The End date day. */
	private EditText EndDateDay;
	
	/** The Description. */
	private EditText Description; 
	
	/** The Des name. */
	private EditText DesName;
	
	/** The Des rea. */
	private EditText DesRea;
	
	/** The Tag name. */
	private EditText TagName;
	
	/** The des list view. */
	private ListView desListView;
	
	/** The tag list view. */
	private ListView tagListView;
	
	/** The value. */
	private Editable value;
	
	/** The tags of claim. */
	private final HashSet<Tag> tagsOfClaim = new HashSet<Tag>();
	
	/** The check correctness. */
	private Boolean checkCorrectness;

	/** The Destination. */
	private ArrayList<String[]> Destination;
	
	/** The adapter2. */
	private ArrayAdapter<String> adapter2;
	
	/** The new destination. */
	private final int newDestination = 1;
	
	/** The edit destination. */
	private final int editDestination = 2;
	
	/** The do nothing. */
	private final int doNothing = 5;
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		
		/**Initialize the dummy destination 2d array which will 
		be used to store destination and reason of travel for both edit claim and create new claim.*/
		
		Destination = new ArrayList<String[]>();
		
		final Claim newclaim = new Claim("");
		
		/**
		 *  Assign each EditText to a variable.
		 */
		
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
		
		
		/**
		 * On click for add tags button
		 */
				
		Button b_add_tag = (Button) findViewById(R.id.buttonEditTags);
		
		b_add_tag.setOnClickListener(new Button.OnClickListener(){
		    /**
		     * @see android.view.View.OnClickListener#onClick(android.view.View)
		     */
		    public void onClick(View v) {
		    	
		    	getAndSetTag();
		    }
		});
		
		/**
		 * Tag List Item click listener
		 */
		tagListView.setOnItemClickListener( new OnItemClickListener() {
			
        	/** Make the items in the tag list clickable
        	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
        	 */
        	@Override
			public void onItemClick(AdapterView<?> parent,
					View view, final int position, long id) {
				
				PopupMenu popup = new PopupMenu(EditClaimActivity.this, view);
				popup.getMenuInflater().inflate(R.menu.edit_claim_tag_list_popup, popup.getMenu());
				
				/**
				 *  Popup menu item click listener
				 */
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
		
		
		/**
		 * Get date from Main activity through the controller.
		 * Get claim id (UUID) if edit claim is selected from Main activity.
		 */
		
		final Intent intent = this.getIntent();
	    final boolean isNewClaim = (boolean) intent.getBooleanExtra("isNewClaim", true);
	    final ClaimList newclaimlist = ClaimController.getInstance(EditClaimActivity.this).getClaimList();
	    
	    UUID serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
	    final Claim claim = ClaimController.getInstance(EditClaimActivity.this).getClaimList().getClaim(serialisedId);
	    
	    /**
	     * On click listener for add destination button in EditClaimActivity.
	     */
		Button editDestinationButton = (Button) findViewById(R.id.buttonAddDestination);
		
		editDestinationButton.setOnClickListener(new View.OnClickListener() {	
			/** Make add destination button clickable
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				/** check if the user pressed create new claim or edit existing claim button from MainActivity.*/
				if (isNewClaim == true){
					createDestinationButton(isNewClaim,Destination,newDestination,doNothing);
				} else {
					Destination = claim.getDestination();
					createDestinationButton(isNewClaim, Destination,newDestination,doNothing);
				}
			}
		});
		
		Button done = (Button) findViewById(R.id.buttonCreateClaim);
		
		/**
		 * Checks if the user wants to edit an existing claim or create a new claim.
		 * If existing claim, set text to the completed ListViews and ExitTexts.
		 */
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
			
			/** Saving new tags */
			ArrayList<Tag> current = TagController.getInstance(this).getTagMap().getTags();
			for ( Tag t : tagsOfClaim ){
				if ( ! current.contains(t) ){
					TagController.getInstance(this).getTagMap().addTag(this, t);
				}
			}
	    }
	    
	    /**
	     *  On item click for the destination and reason ListView.
	     */
	    desListView.setOnItemClickListener(onListClick);
	    
	    /**
		 * On click listener for edit claim/create claim button (button name will change depending on what button
		 * was pressed from the previous activity). Saving the edited/new claim will be triggered only when the 
		 * edit claim/create claim button is pressed. 
		 */
	    done.setOnClickListener(new View.OnClickListener() {	
			/** make edit/create claim button clickable
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				
				/** this procedure will check if the claim name is repeated */
				boolean repeatedClaimName = false;
				Claim[] claims = ClaimController.getInstance(EditClaimActivity.this).getClaimList().getAllClaims();
				for ( Claim c : claims ){
						if ( c.getClaimName().equals( ClaimTitle.getText().toString() )
							&& ( isNewClaim || ! c.getUuid().equals(claim.getUuid())) ){
						repeatedClaimName = true;
					}
				}
				
				/** this statement checks if the text fields are valid or not and display error message.*/
				if( ClaimName.getText().toString().length() == 0 || ClaimTitle.getText().toString().length() == 0 ){
				    if ( ClaimName.getText().toString().length() == 0 ){
				    	ClaimName.setError( "Name is required!" );
				    	ClaimName.requestFocus();
				    }
				    else if ( ClaimTitle.getText().toString().length() == 0 ){
				    	ClaimTitle.setError( "Title is required!" );
				    	ClaimTitle.requestFocus();
				    }
				} else if (repeatedClaimName) {
					ClaimTitle.setError( "Repeated claim name!" );
			    	ClaimTitle.requestFocus();
				} else {
					
				/**
				 *  Saves user input into claim class.(calling each method)
				 */
					Toast.makeText(EditClaimActivity.this, "Updating", Toast.LENGTH_SHORT). show();
					
					if (isNewClaim == true){
						editclaim(newclaim);
						newclaimlist.addClaim(EditClaimActivity.this, newclaim);
						newclaim.setDestination(EditClaimActivity.this, Destination);
					} else{
						editclaim(claim);
						claim.setDestination(EditClaimActivity.this, Destination);
					}
					
					/**
					 *  launch MainClaimActivity.
					 */
					Intent intent = new Intent(EditClaimActivity.this, MainActivity.class);
					startActivity(intent);
				}
			}
		});
	    
	    /**
	     * On click for Cancel button. Calls "safe guard" method if user accidently 
	     * pressed cancel.
	     */
	    Button cancel = (Button) findViewById(R.id.button_cancel_edit_claim);
	    cancel.setOnClickListener(new View.OnClickListener() {
			
			/** Make cancel button clickable
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				cancelcheck();
				
			}
		});
	    
	}
	

	/**
     * On click listener for the back button(soft key). Calls "safe guard" method if user accidently 
     * pressed back button.
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        //do your stuff
	    	cancelcheck();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	/** Destroy this activity when done. 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop(){
		super.onStop();
		finish();
	}
	
	/**
	 * Gets the and set tag.
	 * displays a popup autocomplete text view so the user can enter
	 * a tag name, and then updates the list view
	 * @return the and set tag
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
			/**
			 * @see android.view.View.OnFocusChangeListener#onFocusChange(android.view.View, boolean)
			 */
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
	        /**
	         * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	         */
	        public void onClick(DialogInterface dialog, int whichButton) {       		
	        	value = input.getText();
		    	if ( input.getText() != null ){
		    		//Tag newTag = new Tag(input.getText().toString());
		    		Tag newTag;
					try {
						newTag = TagController.getInstance(getBaseContext()).getTagMap().
								searchForTagByString(input.getText().toString());
						tagsOfClaim.add(newTag);
			    		updateTagListView(new ArrayList<Tag>(tagsOfClaim));
					} catch (IllegalAccessException e) {
			    		Toast.makeText(getApplicationContext(), "Tag does not exist.", Toast.LENGTH_SHORT).show();
			    		value = null;
			    		return;
					}
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
		    	
		    	
/*		    	if (input.getText() == null){
		    		.setError( "Name is required!" );
		    	}
		    	
*/
		    	value = null;
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	
	        /** 
	         * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	         */
	        public void onClick(DialogInterface dialog, int whichButton) {
	            /** Do nothing */
	        }
	    }).show();
		
	}


	/**
	 * Make the items in destination ListView clickable and generate 
	 * a popup box asking user what to do.
	 */
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			/**
			 *  http://www.androiddom.com/2011/06/displaying-android-pop-up-dialog_13.html 	2015-03-11
			 */
			AlertDialog.Builder helperBuilder = new AlertDialog.Builder(EditClaimActivity.this);
			
			helperBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener(){
				/** When edit button is pressed, opens the destination popup
				 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
				 */
				public void onClick(DialogInterface dialog, int which){
					createDestinationButton(false, Destination,editDestination,position);
				}
			});
			
			helperBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

				/** Back out the activity without doing anything
				 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
				 */
				@Override
				  public void onClick(DialogInterface dialog, int which) {
				   /** Do nothing */
				  }
			});
			
			helperBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener(){
			
				/** When the delete button is pressed, the item in the dummy listview will be deleted and adapter updated
				 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
				 */
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
	
	/**
	 * Editclaim.
	 *
	 * @param claim the claim
	 * Get info from user and add it to claim.
	 */
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
		
		/** A check if date is parseable (preventing app from crashing) */
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
		
		for (Tag tag: tagsOfClaim)
			claim.getTagsIds().add(tag.getUuid());

		
		ClaimController.getInstance(this).getClaimList().addClaim(this, claim);		
	}



	/**
	 * Creates the destination button.
	 *
	 * @param isNewClaim the is new claim
	 * @param destination2 the destination2
	 * @param i the i
	 * @param position the position
	 * Create a popup window for entering and editing destination/reason then call save it into the dummy
	 * 2d destination array.
	 */
	private void createDestinationButton( final boolean isNewClaim, final ArrayList<String[]> destination2, final int i,final int position) {

		/**
		 *  http://www.androiddom.com/2011/06/displaying-android-pop-up-dialog_13.html 	2015-03-11
		 */
		AlertDialog.Builder helperBuilder = new AlertDialog.Builder(this);
		helperBuilder.setCancelable(false);
		helperBuilder.setTitle("Destinations");
		
		LayoutInflater inflater = getLayoutInflater();
        View popupview = inflater.inflate(R.layout.activity_popup_destination, null);
        helperBuilder.setView(popupview);
        DesName = (EditText) popupview.findViewById(R.id.inputDestination);
        DesRea = (EditText) popupview.findViewById(R.id.inputDestinationReason);
        		
		switch(i){	
		/** for creating new destination */
		case newDestination:
			helperBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				
				/** Once done, add destination into dummy destination
				 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
				 */
				public void onClick(DialogInterface dialog, int which) {
					
					String Des_Name = DesName.getText().toString();
					String Des_Rea = DesRea.getText().toString();
					editDummyDestination(EditClaimActivity.this, Des_Name, Des_Rea, doNothing, null, newDestination);
				}
			});
			
			helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				/** Return to EditClaimActivity doing nothing
				 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
				 */
				public void onClick(DialogInterface dialog, int which) {
							
				}
			});
					
			AlertDialog helperDialog = helperBuilder.create();
			helperDialog.show();
			break;
			
		/** for editing a existing destination */ 
		case editDestination:
			DesName.setText(destination2.get(position)[0]);
			DesRea.setText(destination2.get(position)[1]);
			final String oldDestination = destination2.get(position)[0]+" - "+destination2.get(position)[1];
			
			helperBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				
				/** update destination dummy list
				 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
				 */
				public void onClick(DialogInterface dialog, int which) {
					String Des_Name2 = DesName.getText().toString();
					String Des_Rea2 = DesRea.getText().toString();
					editDummyDestination(EditClaimActivity.this, Des_Name2, Des_Rea2, position, oldDestination,editDestination);
				}
			});
			
			
			helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				/** Do nothing and return
				 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
				 */
				public void onClick(DialogInterface dialog, int which) {
							
				}
			});
					
			AlertDialog helpDialog = helperBuilder.create();
			helpDialog.show();
			break;
		}
				
	}
	
	/**
	 * Update tag list view.
	 *
	 * @param tagList the tag list
	 */
	private void updateTagListView( ArrayList<Tag> tagList ){
		if ( tagList == null ){
			tagList = TagController.getInstance(this).getTagMap().getTags();
		}
		MainTagListAdapter adapter = new MainTagListAdapter(this, tagList);
		tagListView.setAdapter(adapter);
	}
	
	/**
	 * Edits the dummy destination.
	 *
	 * @param context the context
	 * @param place the place
	 * @param Reason the reason
	 * @param position the position
	 * @param oldDestination the old destination
	 * @param i the i

	// Update the adapter and dummy destination arrayList.
	 */
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
	
	/**
	 * Destination listview.
	 *
	 * @param myListView the my list view
	 * @param destination the destination
	// set adapter for destination
	*/
	public void DestinationListview(ListView myListView, ArrayList<String[]> destination){
		
		ArrayList<String> destinationArray = destinationReason(destination);
		adapter2 = new ArrayAdapter<String>(this,  
		          R.layout.edit_claim_listview, 
		          destinationArray);
		myListView.setAdapter(adapter2);
	}
	
	/**
	 * Destination reason.
	 *
	 * @param destination2 the destination2
	 * @return the array list
	// Concatenate destination into one string to display it on simple ListView adapter
	 */
	public ArrayList<String> destinationReason(ArrayList<String[]> destination2){
		final ArrayList<String> destinationreason = new ArrayList<String>();
		String destination_reason = "";
		for (int i = 0; i< destination2.size(); i++){
			destination_reason = destination2.get(i)[0]+ " - " + destination2.get(i)[1];
			destinationreason.add(destination_reason);
		}
		return destinationreason;
	}
	
	/**
	 * Cancelcheck.
	 */
	public void cancelcheck(){
		AlertDialog.Builder helperBuilder = new AlertDialog.Builder(EditClaimActivity.this);
		helperBuilder.setCancelable(false);
		helperBuilder.setTitle("Warning");
		helperBuilder.setMessage("Are you sure you want to exit before saving?");
		helperBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener(){
			
			/** make Cancel button clickable
			 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
			 */
			public void onClick(DialogInterface dialog, int which){
								
				Toast.makeText(EditClaimActivity.this, "Canceling", Toast.LENGTH_SHORT). show();
								
				/** launch MainActivity */
				onStop();
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

}
