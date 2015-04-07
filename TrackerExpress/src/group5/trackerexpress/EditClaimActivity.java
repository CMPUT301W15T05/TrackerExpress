package group5.trackerexpress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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
import android.os.Bundle;
import android.text.Editable;
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
	private EditText ClaimName;
	
	/** The Claim title. */
	private EditText ClaimTitle;
	
	/** The Start date year. */
	private Button StartDateYear;
	
	/** The End date year. */
	private Button EndDateYear;
	
	/** The Description. */
	private EditText Description; 
	
	EditText DesName;
    EditText DesRea;
	
	/** The Tag name. */
	private EditText TagName;
	
	private Location location;
	
	/** The des list view. */
	private ListView desListView;
	
	/** The tag list view. */
	private ListView tagListView;
	
	/** The value. */
	private Editable value;
	
	/** The tags of claim. */
	final private HashSet<Tag> tagsOfClaim = new HashSet<Tag>();
	
	/** The check correctness. */
	private Boolean checkCorrectness;
	private Boolean comingFromMap = false;

	/** The Destination. */
	private ArrayList<Destination> destination;
	
	/** The adapter2. */
	private ArrayAdapter<String> adapter2;
	
	private ArrayAdapter<Tag> adapter;
	
	/** The new destination. */
	private final int newDestination = 1;
	
	/** The edit destination. */
	private final int editDestination = 2;
	
	private int clicked_destination = -1;
	
	/** The do nothing. */
	private final int doNothing = 5;
	
	private Date startDate; 
	
	private Date endDate;
	
	/** The my calendar. */
	private Calendar myCalendar;
	
	private Calendar myCalendar2;
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		
		/**Initialize the dummy destination 2d array which will 
		be used to store destination and reason of travel for both edit claim and create new claim.*/
		
		destination = new ArrayList<Destination>();
		
		final Claim newclaim = new Claim("");
		
		/**
		 *  Assign each EditText to a variable.
		 */
		
		myCalendar = Calendar.getInstance();
		myCalendar2 = Calendar.getInstance();
		
		ClaimName = (EditText) findViewById(R.id.editClaimName);	
		limitLength(ClaimName, 20);
		ClaimName.requestFocus();
		
		ClaimTitle = (EditText) findViewById(R.id.editClaimTitle);
		limitLength(ClaimTitle, 20);
		
<<<<<<< HEAD
=======
		StartDateYear = (Button) findViewById(R.id.editClaimStartDateYear);

		EndDateYear = (Button) findViewById(R.id.editClaimEndDateYear);

>>>>>>> 414745fa09f80138444fcdaca05c525dfd376fe2
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
	    final ClaimList newclaimlist = Controller.getClaimList(EditClaimActivity.this);
	    
	    UUID serialisedId = (UUID) intent.getSerializableExtra("claimUUID");
	    final Claim claim = Controller.getClaimList(EditClaimActivity.this).getClaim(serialisedId);
	    
	    /**
		 * http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
		 * Mar/24/2015
		 */

		if (isNewClaim == true){
			StartDateYear.setOnClickListener(new TextView.OnClickListener(){
				public void onClick(View v) {
					showDatePickerDialog(v, myCalendar);
				}
			});
		
			EndDateYear.setOnClickListener(new TextView.OnClickListener(){
				public void onClick(View v) {
					showDatePickerDialog(v, myCalendar2);
				}
			});
		} else{
			
			if (claim.getStartDate() == null || claim.getEndDate() == null){
				if (claim.getStartDate() == null){
					myCalendar = Calendar.getInstance();
				}
				if (claim.getEndDate() == null){
					myCalendar2 = Calendar.getInstance();
				}
			}
			
			if (claim.getStartDate() != null || claim.getEndDate() != null){
				if(claim.getStartDate() != null){
					myCalendar = claim.getStartDate();
				}
				if (claim.getEndDate() != null){
					myCalendar2 = claim.getEndDate();
				}
			}
			
			
			StartDateYear.setOnClickListener(new TextView.OnClickListener(){
				public void onClick(View v) {
					showDatePickerDialog(v, myCalendar);
				}
			});
		
			EndDateYear.setOnClickListener(new TextView.OnClickListener(){
				public void onClick(View v) {
					showDatePickerDialog(v, myCalendar2);
				}
			});
			
		}
	    
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
				if (isNewClaim != true){
					destination = claim.getDestinationList();
				}
				createDestinationButton(destination,newDestination,doNothing);
			}
		});
		
		Button done = (Button) findViewById(R.id.buttonCreateClaim);
		
		/**
		 * Checks if the user wants to edit an existing claim or create a new claim.
		 * If existing claim, set text to the completed ListViews and ExitTexts.
		 */
	    if (isNewClaim == true){
		    done.setText("Create Claim");
		    DestinationListview(desListView,destination);
		    updateTagListView(new ArrayList<Tag>(tagsOfClaim));
		    if (Controller.getUser(EditClaimActivity.this).getName().toString()!=null){
		    	ClaimName.setText(Controller.getUser(EditClaimActivity.this).getName().toString());
		    }
		   		
	    } else {
	    	String tags = claim.toStringTags(this);
		   	done.setText("Edit Claim");
		   	destination = claim.getDestinationList();
		    ClaimName.setText(claim.getSubmitterName());
			ClaimTitle.setText(claim.getClaimName());
					
			if ( claim.getStartDate() != null ){
				StartDateYear.setText(sdf.format(claim.getStartDate().getTime()));
/*				StartDateMonth.setText(String.valueOf(claim.getStartDate().getMM()));
				StartDateDay.setText(String.valueOf(claim.getStartDate().getDD()));
				*/
			}
					
			if ( claim.getEndDate() != null ){
				EndDateYear.setText(sdf.format(claim.getEndDate().getTime()));
/*				EndDateMonth.setText(String.valueOf(claim.getEndDate().getMM()));
				EndDateDay.setText(String.valueOf(claim.getEndDate().getDD()));
				*/
			}
			Description.setText(String.valueOf(claim.getDescription()));
			DestinationListview(desListView,destination);
			
			try {
				StringTagToArray(tags);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			updateTagListView(new ArrayList<Tag>(tagsOfClaim));
			
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
/*				boolean bothdates = false;
				
				if (StartDateYear.getText().toString().length() > 0
					&& EndDateYear.getText().toString().length() > 0){
					myCalendar.set
					myCalendar2
					double i = myCalendar.compareTo(myCalendar2);
				}
*/	
				Claim[] claims = Controller.getClaimList(EditClaimActivity.this).toList();
				for ( Claim c : claims ){
						if ( c.getClaimName().equals( ClaimTitle.getText().toString() )
							&& ( isNewClaim || ! c.getUuid().equals(claim.getUuid())) ){
						repeatedClaimName = true;
					}
				}
			
				/** this statement checks if the text fields are valid or not and display error message.*/
/*				if( ClaimName.getText().toString().length() == 0 || ClaimTitle.getText().toString().length() == 0 ){
				    if ( ClaimName.getText().toString().length() == 0 ){
				    	ClaimName.setError( "Name is required!" );
				    	ClaimName.requestFocus();
				    }
				    else if ( ClaimTitle.getText().toString().length() == 0 ){
				    	ClaimTitle.setError( "Title is required!" );
				    	ClaimTitle.requestFocus();
				    }
				}
				if (myCalendar.compareTo(myCalendar2)>0){
					EndDateYear.setError("End date is SMALLER than start date!");
					EndDateYear.requestFocus();
				}*/
				myCalendar.set(Calendar.HOUR, 0);
				myCalendar.set(Calendar.MINUTE, 0);
				myCalendar.set(Calendar.SECOND, 0);
				myCalendar.set(Calendar.MILLISECOND, 0);
				myCalendar2.set(Calendar.HOUR, 0);
				myCalendar2.set(Calendar.MINUTE, 0);
				myCalendar2.set(Calendar.SECOND, 0);
				myCalendar2.set(Calendar.MILLISECOND, 0);
				if (repeatedClaimName || ClaimName.getText().toString().length() == 0 || ClaimTitle.getText().toString().length() == 0) {

					if (repeatedClaimName){
						ClaimTitle.setError( "Repeated claim name!" );
						ClaimTitle.requestFocus();
					} 
					else if ( ClaimName.getText().toString().length() == 0 ){
				    	ClaimName.setError( "Name is required!" );
				    	ClaimName.requestFocus();
				    }
				    else if ( ClaimTitle.getText().toString().length() == 0 ){
				    	ClaimTitle.setError( "Title is required!" );
				    	ClaimTitle.requestFocus();
				    }
				
				} else if ( myCalendar.compareTo(myCalendar2) == 1 ){
					Toast.makeText(getApplicationContext(), "End Date cannot be before Start Date!", Toast.LENGTH_SHORT).show();
				} else {
				/**
				 *  Saves user input into claim class.(calling each method)
				 */
					Toast.makeText(EditClaimActivity.this, "Updating", Toast.LENGTH_SHORT). show();
					
					/** Saving new tags */
					
					ArrayList<Tag> current = Controller.getTagMap(EditClaimActivity.this).toList();
					for ( Tag t : tagsOfClaim ){
						if ( ! current.contains(t) ){
							Controller.getTagMap(EditClaimActivity.this).addTag(EditClaimActivity.this, t);
						}
					}
					
					if (isNewClaim == true){
						editclaim(newclaim);
						newclaimlist.addClaim(EditClaimActivity.this, newclaim);
						newclaim.setDestinationList(EditClaimActivity.this, destination);
						checkCompleteness(newclaim);
						
						
					} else{
						Log.i("myMessage", "2");
						editclaim(claim);
						claim.setDestinationList(EditClaimActivity.this, destination);
						checkCompleteness(claim);
						
					}
					
					/**
					 *  launch MainClaimActivity.
					 */
					finish();
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
				cancelCheck(EditClaimActivity.this);				
			}
		});
	    if (claim != null){
	    	if (claim.getStatus() == Claim.SUBMITTED || claim.getStatus() == Claim.APPROVED) {
	    		ClaimName.setFocusable(false);
	    		ClaimTitle.setFocusable(false);
	    		StartDateYear.setClickable(false);
	    		EndDateYear.setClickable(false);
	    		Description.setFocusable(false);
	    		desListView.setEnabled(false);
	    		editDestinationButton.setClickable(false);
	    		editDestinationButton.setVisibility(View.GONE);
	    		//from http://stackoverflow.com/questions/4989545/make-edittext-behave-as-a-textview-in-code accessed 06/04/2015
	    		ClaimName.setBackgroundResource(android.R.color.transparent);
	    		ClaimTitle.setBackgroundResource(android.R.color.transparent);
	    		Description.setBackgroundResource(android.R.color.transparent);
	    		//from http://stackoverflow.com/questions/8743120/how-to-grey-out-a-button accessed 06/04/2015
	    		StartDateYear.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.CLEAR);
	    		EndDateYear.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.CLEAR);
	    		done.setText("Edit Tags");
	    	}
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
		if (view == StartDateYear) {
			StartDateYear.setText(sdf.format(date.getTime()));
			myCalendar = date;
		} else if (view == EndDateYear) {
				EndDateYear.setText(sdf.format(date.getTime()));
				myCalendar2 = date;
			}
		
    }
	
	
	/** check if the claim is completed*/
	private void checkCompleteness(Claim claim){
		if (ClaimName.getText().toString().length() > 0 && ClaimTitle.getText().toString().length() > 0 &&
				Description.getText().toString().length() > 0 && claim.getStartDate() != null /*&& claim.getEndDate() != null*/
				&& claim.getDestinationList().size() >= 1){
			claim.setIncomplete(this, false);
		}else {
			claim.setIncomplete(this, true);
		}
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

		ArrayList<Tag> tagList = Controller.getTagMap(EditClaimActivity.this).toList();
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
		    		Tag newTag = null;
					try {
						newTag = Controller.getTagMap(getBaseContext()).
								searchForTagByString(input.getText().toString());
						tagsOfClaim.add(newTag);
			    		updateTagListView(new ArrayList<Tag>(tagsOfClaim));
					} catch (IllegalAccessException e) {
			    		Toast.makeText(getApplicationContext(), "New Tag", Toast.LENGTH_SHORT).show();
			    		newTag = new Tag(value.toString());
			    		tagsOfClaim.add(newTag);
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
					createDestinationButton(destination,editDestination,position);
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
					destination.remove(position);
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
		String aftermath = StartDateYear.getText().toString();
		String aftermath2 = EndDateYear.getText().toString();

		Calendar d2 = Calendar.getInstance();
		Calendar d1 = Calendar.getInstance();
		String claimUser = ClaimName.getText().toString();
		String Claim_title = ClaimTitle.getText().toString();
		
		String Descrip = Description.getText().toString();
		
		d2 = myCalendar2;
		
		d1 = myCalendar;
			
		if (aftermath.length() > 0 || aftermath2.length() > 0){
			if ( aftermath.length() > 0 ){
				Log.i("myMessage", "1");
		    	claim.setStartDate(this, d1);
		    	Log.i("myMessage", "1.2");
		    }
		    if ( aftermath2.length() > 0 ){
		    	claim.setEndDate(this, d2);
		    }
		}

		claim.setSubmitterName(this, claimUser);
		claim.setClaimName(this, Claim_title);

		
		
		claim.setDescription(this, Descrip);
		
		claim.getTagsIds(this).clear();
		
		for (Tag tag: tagsOfClaim)
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
				System.out.println("Latitude was set as " + destination.get(position).getLatitude());
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
		
		helperBuilder.setNeutralButton("Tag Location?", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.e("NEUTRAL", "NEUTRAL");
				Intent intent = new Intent(EditClaimActivity.this, MapActivity.class);
				
				if (!desName.getText().toString().isEmpty()) {
					System.out.println("Putting extra destination " + desName.getText().toString());
					Log.e("DESTINATION", "Putting extra destination " + desName.getText().toString());
					intent.putExtra("destination", desName.getText().toString());
				}
				
				if (location != null) {
					System.out.println("Putting extra location");
					Log.e("LOC", "Putting extra location " + desName.getText().toString());
					LatLng newlatlng = new LatLng(location.getLatitude(), location.getLongitude());
					intent.putExtra("latlng", newlatlng);
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
				System.out.println("Latitude set as " + destination.get(position).getLatitude());
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
		        createDestinationButton(destination, newDestination,clicked_destination);
	        } else {
		        createDestinationButton(destination, editDestination,clicked_destination);
	        }
	        DesRea.setText(lastRea);
	        
	        if(resultCode == RESULT_OK){
	            LatLng latLng = data.getParcelableExtra("resultLatLng");
	            String title = data.getStringExtra("resultTitle");
	            System.out.println("TITLE IS " + title);
	            
	            if (location == null) {
	            	location = new Location("");
	            }
	            
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

		adapter = new ArrayAdapter<Tag>(this,  
		          R.layout.edit_claim_listview, 
		          tagList);
		tagListView.setAdapter(adapter);
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
			adapter2.add(place + " - " + reason);
			adapter2.notifyDataSetChanged();
			destination.add(travelInfo);
			break;
		case editDestination:
			adapter2.insert(place + " - " + reason, position);
			adapter2.remove(oldDestination);
			destination.set(position,travelInfo);
			adapter2.notifyDataSetChanged();
		}	
	}
	
	/**
	 * Destination listview.
	 *
	 * @param myListView the my list view
	 * @param destination2 the destination
	// set adapter for destination
	*/
	public void DestinationListview(ListView myListView, ArrayList<Destination> destination2){
		
		ArrayList<String> destinationArray = destinationReason(destination2);
		adapter2 = new ArrayAdapter<String>(this,  
		          R.layout.edit_claim_listview, 
		          destinationArray);
		myListView.setAdapter(adapter2);
	}
	
	
	private void StringTagToArray(String stringTag) throws IllegalAccessException {
		// TODO Auto-generated method stub
		Tag newTag;
		String[] part = stringTag.split(", ");
		for (int i =0; i < part.length; i++){
			newTag = Controller.getTagMap(getBaseContext()).
					searchForTagByString(part[i]);
			tagsOfClaim.add(newTag);
		}
	}


	/**
	 * Destination reason.
	 *
	 * @param destination2 the destination2
	 * @return the array list
	// Concatenate destination into one string to display it on simple ListView adapter
	 */
	public ArrayList<String> destinationReason(ArrayList<Destination> destination2){
		final ArrayList<String> destinationreason = new ArrayList<String>();
		String destination_reason = "";
		for (int i = 0; i< destination2.size(); i++){
			destination_reason = destination2.get(i).toString();
			destinationreason.add(destination_reason);
		}
		return destinationreason;
	}

}
