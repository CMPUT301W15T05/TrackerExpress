package group5.trackerexpress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * The Class EditClaimActivity.
 */
/**
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 *
 */
public class EditClaimActivity extends EditableActivity implements DatePickerFragment.TheListener{
	
	final String myFormat = "EEEE MMMM dd, yyyy"; //In which you need put here
	final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
	
	/** The Claim name. */
	private EditText claimNameView;
	
	/** The Claim title. */
	private EditText claimTitleView;
	
	/** The Start date year. */
	private Button startDateButton;
	
	/** The End date year. */
	private Button endDateButton;
	
	/** The Description. */
	private EditText descriptionView; 
	
	EditText DesName;
    EditText DesRea;
	
    /** location of destination */
    private Location location;
	
	/** The des list view. */
	private ListView desListView;
	
	/** The tag list view. */
	private ListView tagListView;
	
	/** The tags of claim. */
	private ArrayList<Tag> tagList;
	
	/** The check correctness. */
	private Boolean comingFromMap = false;

	/** The Destination. */
	private ArrayList<Destination> destinationList;
	
	/** The adapter for the Destination List. */
	private ArrayAdapter<String> destinationAdapter;
	
	/** The array adapter for the Tag List */
	private ArrayAdapter<Tag> tagAdapter;
	
	/** The new destination. */
	private final int newDestination = 1;
	
	/** The edit destination. */
	private final int editDestination = 2;
	
	private int clicked_destination = -1;
	
	/** The do nothing. */
	private final int doNothing = 5;
	
	/** The my calendar. */
	private Calendar startDateSelection;
	
	private Calendar endDateSelection;
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		
		/**Initialize the dummy destination 2d array which will 
		be used to store destination and reason of travel for both edit claim and create new claim.*/
		
		destinationList = new ArrayList<Destination>();
		tagList = new ArrayList<Tag>();
		
		final Claim newclaim = new Claim("");
		
		/**
		 *  Assign each EditText to a variable.
		 */
		
		claimNameView = (EditText) findViewById(R.id.editClaimName);	
		limitLength(claimNameView, 20);
		claimNameView.requestFocus();
		
		claimTitleView = (EditText) findViewById(R.id.editClaimTitle);
		limitLength(claimTitleView, 20);
		
		startDateButton = (Button) findViewById(R.id.editClaimStartDate);
		endDateButton = (Button) findViewById(R.id.editClaimEndDate);
		descriptionView = (EditText) findViewById(R.id.editClaimDescription);
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
		    	
		    	showTagPopUp();
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
                        	tagList.remove(t);
                        	updateTagListView(tagList);
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
	    final ClaimList newClaimList = Controller.getClaimList(EditClaimActivity.this);
	    
	    UUID serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
	    final Claim claim = newClaimList.getClaim(serialisedId);
	    
	    /**
		 * http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
		 * Mar/24/2015
		 */

		if (isNewClaim == true){
			startDateSelection = Calendar.getInstance();
			endDateSelection = Calendar.getInstance();
		} else{
			startDateSelection = claim.getStartDate();
			endDateSelection = claim.getEndDate();
		}
			
			
		startDateButton.setOnClickListener(new TextView.OnClickListener(){
			public void onClick(View v) {
				showDatePickerDialog(v, startDateSelection);
			}
		});
		
		endDateButton.setOnClickListener(new TextView.OnClickListener(){
			public void onClick(View v) {
				showDatePickerDialog(v, endDateSelection);
			}
		});
	    
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
				
				
				/**
				 * The if statement checks for network connectivity, if network is connected, 
				 * add destination button willwork. If not message will be displayed to remind user.
				 */
				
				if (Controller.isInternetConnected(getApplicationContext())){
					/** check if the user pressed create new claim or edit existing claim button from MainActivity.*/
					if (isNewClaim != true){
						destinationList = claim.getDestinationList();
					}
					createDestinationButton(destinationList, newDestination,doNothing);
				}else{
					Toast.makeText(getApplicationContext(), "This function requires a network!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		Button finishedButton = (Button) findViewById(R.id.buttonCreateClaim);
		
		/**
		 * Checks if the user wants to edit an existing claim or create a new claim.
		 * If existing claim, set text to the completed ListViews and ExitTexts.
		 */
	    if (isNewClaim == true){
		    finishedButton.setText("Create Claim");
		    if (Controller.getUser(EditClaimActivity.this).getName().toString()!=null) {
		    	claimNameView.setText(Controller.getUser(EditClaimActivity.this).getName().toString());
		    }
		    
		   		
	    } else {
		   	finishedButton.setText("Edit Claim");
		   	destinationList = claim.getDestinationList();
		    claimNameView.setText(claim.getSubmitterName());
			claimTitleView.setText(claim.getClaimName());
			descriptionView.setText(String.valueOf(claim.getDescription()));
			
			try {
				StringTagToArray(claim.toStringTags(this));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			updateTagListView(tagList);
	    }
	    
	    setDestinationAdapter(destinationList);
	    startDateButton.setText(sdf.format(startDateSelection.getTime()));
	    endDateButton.setText(sdf.format(endDateSelection.getTime()));
	    
	    /**
	     *  On item click for the destination and reason ListView.
	     */
	    desListView.setOnItemClickListener(onDestinationListClick);
	    
	    
	    /**
		 * On click listener for edit claim/create claim button (button name will change depending on what button
		 * was pressed from the previous activity). Saving the edited/new claim will be triggered only when the 
		 * edit claim/create claim button is pressed. 
		 */
	    finishedButton.setOnClickListener(new View.OnClickListener() {	
			/** make edit/create claim button clickable
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
		        
				/** this procedure will check if the claim name is repeated */
				Claim[] claims = Controller.getClaimList(EditClaimActivity.this).toList();
				String sTitle = claimTitleView.getText().toString();
				for ( Claim c : claims ){
					if ( c.getClaimName().equals( sTitle )
							&& ( isNewClaim || ! c.getUuid().equals(claim.getUuid())) ) {
						claimTitleView.setError( "Repeated claim name!" );
						claimTitleView.requestFocus();
						return;
					}
				}
			
				/** this statement checks if the text fields are valid or not and display error message.*/

				
				if ( claimNameView.getText().toString().isEmpty() ){
			    	claimNameView.setError( "Name is required!" );
			    	claimNameView.requestFocus();
			    	return;
			    }
			    else if ( claimTitleView.getText().toString().isEmpty() ){
			    	claimTitleView.setError( "Title is required!" );
			    	claimTitleView.requestFocus();
			    	return;
			    }

				startDateSelection.set(Calendar.HOUR, 0);
				startDateSelection.set(Calendar.MINUTE, 0);
				startDateSelection.set(Calendar.SECOND, 0);
				startDateSelection.set(Calendar.MILLISECOND, 0);
				endDateSelection.set(Calendar.HOUR, 0);
				endDateSelection.set(Calendar.MINUTE, 0);
				endDateSelection.set(Calendar.SECOND, 0);
				endDateSelection.set(Calendar.MILLISECOND, 0);
				
				if (startDateSelection.compareTo(endDateSelection) == 1) {
					Toast.makeText(getApplicationContext(), "End Date cannot be before Start Date!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				/**
				 *  Saves user input into claim class.(calling each method)
				 */
				Toast.makeText(EditClaimActivity.this, "Updating", Toast.LENGTH_SHORT). show();
					
				/** Saving new tags */
					
				TagMap tagMap = Controller.getTagMap(EditClaimActivity.this);
					
				for ( Tag t : tagList ){
					if ( ! tagMap.contains(t.getUuid()) ){
						tagMap.addTag(EditClaimActivity.this, t);
					}
				}
					
				if (isNewClaim == true) {
					editclaim(newclaim);
					newClaimList.addClaim(EditClaimActivity.this, newclaim);
					newclaim.setDestinationList(EditClaimActivity.this, destinationList);
					checkCompleteness(newclaim);
						
				} else{
					editclaim(claim);
					claim.setDestinationList(EditClaimActivity.this, destinationList);
					checkCompleteness(claim);
						
				}
					
				/**
				 *  Return to the activity that called this.
				 */
				finish();
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
				cancelCheck(EditClaimActivity.this);				
			}
		});
	    
	    if (!isNewClaim && (claim.getStatus() == Claim.SUBMITTED || claim.getStatus() == Claim.APPROVED)) {
	    	claimNameView.setFocusable(false);
	    	claimTitleView.setFocusable(false);
	    	startDateButton.setClickable(false);
	    	endDateButton.setClickable(false);
	    	descriptionView.setFocusable(false);
	    	desListView.setEnabled(false);
	    	editDestinationButton.setClickable(false);
	    	editDestinationButton.setVisibility(View.GONE);
	    	//from http://stackoverflow.com/questions/4989545/make-edittext-behave-as-a-textview-in-code accessed 06/04/2015
	    	claimNameView.setBackgroundResource(android.R.color.transparent);
	    	claimTitleView.setBackgroundResource(android.R.color.transparent);
	    	descriptionView.setBackgroundResource(android.R.color.transparent);
	    	//from http://stackoverflow.com/questions/8743120/how-to-grey-out-a-button accessed 06/04/2015
	    	startDateButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.CLEAR);
	    	endDateButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.CLEAR);
	    	finishedButton.setText("Edit Tags");
	    }
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
	    	cancelCheck(EditClaimActivity.this);
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void returnDate(View view, Calendar date) {
		if (view == startDateButton) {
			startDateButton.setText(sdf.format(date.getTime()));
			startDateSelection = date;
		} else if (view == endDateButton) {
			endDateButton.setText(sdf.format(date.getTime()));
			endDateSelection = date;
		}
    }
	
	
	/** check if the claim is completed*/
	private void checkCompleteness(Claim claim){

		if (claimNameView.getText().toString().length() > 0 && claimTitleView.getText().toString().length() > 0 &&
				descriptionView.getText().toString().length() > 0 && claim.getStartDate() != null /*&& claim.getEndDate() != null*/

				&& claim.getDestinationList().size() >= 1){
			claim.setIncomplete(this, false);
		}else {
			claim.setIncomplete(this, true);
		}
	}
	
	/**
	 * Gets the and set tag.
	 * Displays a popup autocomplete text view so the user can enter
	 * a tag name, and then updates the list view
	 * @return the and set tag
	 */
	private void showTagPopUp() {
		
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		
		helpBuilder.setTitle("Create Tag");
	    helpBuilder.setMessage("Enter a new name");
		
	    // Create the AutoComplete input
		final AutoCompleteTextView input = new AutoCompleteTextView(EditClaimActivity.this);

		final TagMap tagMap = Controller.getTagMap(EditClaimActivity.this);
		ArrayList<String> tags = tagMap.getTagStrings(tagList);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditClaimActivity.this, R.layout.edit_claim_drop_down_item, tags);
		input.setAdapter(adapter);
		input.setThreshold(1);
		input.setMaxLines(1);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		
		input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			/**
			 * @see android.view.View.OnFocusChangeListener#onFocusChange(android.view.View, boolean)
			 */
			@Override
			  public void onFocusChange(View view, boolean hasFocus) {
				  if(hasFocus && !input.getText().toString().isEmpty()){
					  input.showDropDown();
				  }
			  }
		});
		
	    helpBuilder.setView(input);
	    
	    helpBuilder.setPositiveButton("Ok", doNothingClicker);
	    helpBuilder.setNegativeButton("Cancel", doNothingClicker);
	    
	    final AlertDialog helpDialog = helpBuilder.create();
	    
	    helpDialog.setOnShowListener(new DialogInterface.OnShowListener() {

	        @Override
	        public void onShow(DialogInterface dialog) {

	            Button b = helpDialog.getButton(AlertDialog.BUTTON_POSITIVE);
	            b.setOnClickListener(new View.OnClickListener() {

	                @Override
	                public void onClick(View view) {
	                	String sInput = input.getText().toString().trim();

	                	if (sInput.isEmpty()) {
	                		input.setError("Enter a tag name");
	                		input.setText(sInput);
	                		return;
	                	}
	                	
	                	// Tag has already been added
	                	for (Tag t : tagList) {
	                		if (t.toString().equals(sInput)) {
		                		input.setError("Tag already added to claim");
		                		input.setText(sInput);
	                			return;
	                		}
	                	}
	                	
	                	Tag newTag;
	    	        	try {
	    	        		newTag = tagMap.searchForTagByString(sInput);
	    	        	} catch (IllegalAccessException e) {
	    	        		Toast.makeText(getApplicationContext(), "New Tag", Toast.LENGTH_SHORT).show();
	    	        		newTag = new Tag(sInput);
	    	        	}
	    	        	
	    	        	tagList.add(newTag);
			    		updateTagListView(tagList);
	                	
	                    helpDialog.dismiss();
	                }
	            });
	        }
	    });

	    helpDialog.show();
	}

	/**
	 * Make the items in destination ListView clickable and generate 
	 * a popup box asking user what to do.
	 */
	private AdapterView.OnItemClickListener onDestinationListClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			/**
			 *  http://www.androiddom.com/2011/06/displaying-android-pop-up-dialog_13.html 	2015-03-11
			 */
			AlertDialog.Builder helperBuilder = new AlertDialog.Builder(EditClaimActivity.this);
			
			helperBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener(){
				
				/** When the delete button is pressed, the item in the dummy listview will be deleted and adapter updated
				 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
				 */
				@Override
				public void onClick(DialogInterface dialog, int which){
					String toRemove = destinationAdapter.getItem(position);
					destinationAdapter.remove(toRemove);
					destinationList.remove(position);
					destinationAdapter.notifyDataSetChanged();
				}
			});
			
			helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				/** Back out the activity without doing anything
				 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
				 */
				@Override
				  public void onClick(DialogInterface dialog, int which) {
				   /** Do nothing */
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
		String aftermath = startDateButton.getText().toString();
		String aftermath2 = endDateButton.getText().toString();

		Calendar d2 = Calendar.getInstance();
		Calendar d1 = Calendar.getInstance();
		String claimUser = claimNameView.getText().toString();
		String Claim_title = claimTitleView.getText().toString();
		
		String Descrip = descriptionView.getText().toString();
		
		d2=endDateSelection;
		
		d1=startDateSelection;
			
		if (aftermath.length() > 0 || aftermath2.length() > 0){
			if ( aftermath.length() > 0 ){
		    	claim.setStartDate(this, d1);
			}
		    if ( aftermath2.length() > 0 ){
		    	claim.setEndDate(this, d2);
		    }
		}

		claim.setSubmitterName(this, claimUser);
		claim.setClaimName(this, Claim_title);

		
		
		claim.setDescription(this, Descrip);
		
		claim.getTagsIds(this).clear();
		
		for (Tag tag: tagList)
			claim.getTagsIds(this).add(tag.getUuid());

		
		Controller.getClaimList(this).addClaim(this, claim);		
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
	private void createDestinationButton(final ArrayList<Destination> destination2, final int i,final int position) {

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
			clicked_destination = -1;
			dialog(helperBuilder,DesName, DesRea, location, doNothing, null, newDestination);
			
			
			break;
			
		/** for editing a existing destination */ 
		case editDestination:
			clicked_destination = position;
			
			if (!comingFromMap) {
				System.out.println("Not coming from map");
				DesName.setText(destination2.get(position).getName());
				DesRea.setText(destination2.get(position).getDescription());
				location = destination2.get(position).getLocation();
			}
			final String oldDestination = destination2.get(position).toString();
			dialog(helperBuilder,DesName, DesRea, location,position, oldDestination, editDestination);
			
			
			break;
		}
				
	}
	
	private void dialog(Builder helperBuilder, final EditText desName, final EditText desRea, Location location2, final int position, final String oldDestination, final int editDestination2){
		
		helperBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			
			/** Once done, add destination into dummy destination
			 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
			 */
			public void onClick(DialogInterface dialog, int which) {
				
				String Des_Name = desName.getText().toString();
				String Des_Rea = desRea.getText().toString();
				editDummyDestination(EditClaimActivity.this, Des_Name, Des_Rea, position, oldDestination, editDestination2);
			}
		});
		
		helperBuilder.setNeutralButton("Add GeoLocation", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.e("NEUTRAL", "NEUTRAL");
				Intent intent = new Intent(EditClaimActivity.this, InteractiveMapActivity.class);
				
				if (location != null) {
					System.out.println("Putting extra location");
					Log.e("LOC", "Putting extra location " + desName.getText().toString());
					LatLng newlatlng = new LatLng(location.getLatitude(), location.getLongitude());
					intent.putExtra("latlng", newlatlng);
					intent.putExtra("destination", location.getProvider());
				} else if (!desName.getText().toString().isEmpty()) {
					System.out.println("Putting extra destination " + desName.getText().toString());
					Log.e("DESTINATION", "Putting extra destination " + desName.getText().toString());
					intent.putExtra("destination", desName.getText().toString());
				}
				
				System.out.println("GOING IN");
				Log.e("START", "GOING IN");
				if (location != null) {
					System.out.println("LOCATION IS 5: " + location.getLatitude() + " " + location.getLongitude());
				} else {
					System.out.println("LOCATION IS NULL 5");
					
				}
		    	EditClaimActivity.this.startActivityForResult(intent, 1);
			}
			
		});
		
		helperBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			/** Return to EditClaimActivity doing nothing
			 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
			 */
			public void onClick(DialogInterface dialog, int which) {
				location = null;
			}
		});
				
		AlertDialog helperDialog = helperBuilder.create();
		helperDialog.show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) {
	    	String lastDest = DesName.getText().toString();
	    	String lastRea = DesRea.getText().toString();
	    	comingFromMap = true;
	        if (clicked_destination == -1) {
		        createDestinationButton(destinationList, newDestination,clicked_destination);
	        } else {
		        createDestinationButton(destinationList, editDestination,clicked_destination);
	        }
	        DesRea.setText(lastRea);
	        
	        if(resultCode == RESULT_OK){
	            LatLng latLng = data.getParcelableExtra("resultLatLng");
	            String title = data.getStringExtra("resultTitle");
	            System.out.println("TITLE IS " + title);
	            
	            if (location == null) {
	            	location = new Location("");
	            }
	            
	            location.setProvider(title);
	            location.setLongitude(latLng.longitude);
	            location.setLatitude(latLng.latitude);
	            
	            
	            if (lastDest.isEmpty()) {
	            	System.out.println("Des is empty " + title);
	            	DesName.setText(title);
	            }
	            
	        } else if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	        
	        if (DesName.getText().toString().isEmpty()) {
            	DesName.setText(lastDest);
	        }
	        

	    	comingFromMap = false;
	    }
	    
	}
	
	/**
	 * Update tag list view.
	 *
	 * @param tagList the tag list
	 */
	private void updateTagListView( ArrayList<Tag> tagList ){
		if ( tagList == null ){
			tagList = Controller.getTagMap(this).toList();
		}

		tagAdapter = new ArrayAdapter<Tag>(this,  
		          R.layout.edit_claim_listview, 
		          tagList);
		tagListView.setAdapter(tagAdapter);
	}
	
	/**
	 * Edits the dummy destination.
	 *
	 * @param context Needed for file IO
	 * @param place the place
	 * @param reason the reason
	 * @param position the position
	 * @param oldDestination the old destination
	 * @param i the i

	// Update the adapter and dummy destination arrayList.
	 */
	public void editDummyDestination(Context context, String place, String reason, int position, String oldDestination, int i){
		Destination travelInfo = new Destination();
		travelInfo.setName(place);
		travelInfo.setDescription(reason); 
		System.out.println("HEREEREJHASJKDHJKASHD");
		travelInfo.setLocation(location);
		location = null;
		switch(i){
		case newDestination:
			destinationAdapter.add(place + " - " + reason);
			destinationAdapter.notifyDataSetChanged();
			destinationList.add(travelInfo);
			break;
		case editDestination:
			destinationAdapter.insert(place + " - " + reason, position);
			destinationAdapter.remove(oldDestination);
			destinationList.set(position,travelInfo);
			destinationAdapter.notifyDataSetChanged();
		}	
	}
	
	/**
	 * Set the destintion list adapter
	 *
	 * @param destinations the destination
	// set adapter for destination
	*/
	public void setDestinationAdapter(ArrayList<Destination> destinations){
		
		ArrayList<String> destinationReasonList = new ArrayList<String>();
		
		for (int i = 0; i< destinations.size(); i++){
			destinationReasonList.add(destinations.get(i).toString());
		}
		
		destinationAdapter = new ArrayAdapter<String>(this,  
		          R.layout.edit_claim_listview, 
		          destinationReasonList);
		desListView.setAdapter(destinationAdapter);
	}
	
	
	private void StringTagToArray(String stringTag) throws IllegalAccessException {
		// TODO Auto-generated method stub
		Tag newTag;
		String[] part = stringTag.split(", ");
		for (int i =0; i < part.length; i++){
			newTag = Controller.getTagMap(getBaseContext()).
					searchForTagByString(part[i]);
			tagList.add(newTag);
		}
	}


	/**
	 * Destination reason.
	 *
	 * @param destinations the destination2
	 * @return the array list
	// Concatenate destination into one string to display it on simple ListView adapter
	 */
	public ArrayList<String> assembleDestinationList(ArrayList<Destination> destinations){
		ArrayList<String> destinationReasonList = new ArrayList<String>();
		for (int i = 0; i< destinations.size(); i++){
			destinationReasonList.add(destinations.get(i).toString());
		}
		
		return destinationReasonList;
	}

}
