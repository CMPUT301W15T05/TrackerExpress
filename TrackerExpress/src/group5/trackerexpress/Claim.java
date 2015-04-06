package group5.trackerexpress;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.content.Context;


/**
 * The Class Claim.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Claim extends TModel implements Comparable<Claim>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The name of the claim creator*/
	private String submitterName;
	private String submitterEmail;
	
	/** The name of the claim creator*/
	private String approverName;
	private String approverEmail;	
	
	/** The claim name. */
	private String claimName;
	
	/** The Description of the claim. */
	private String description;
	
	/** The list of expenses for the claim. */
	private ExpenseList expenseList;
	
	/** The destinations visited in claim. */
	private ArrayList<Destination> destinations;
	
	/** The status of the claim (in_progress, submitted, returned, or approved. */
	private int status; 
	
	/** The start date of the claim. */
	private Calendar startDate;
	
	/** The end date of the claim. */
	private Calendar endDate;
	
	/** The incompleteness indicator. */
	private boolean incomplete;
	
	/** The comments returned from an approver*/
	private String comments;

	/** The ArrayList of tagIds.*/
	private ArrayList<UUID> tagIds;
	
    
	/** The Constant IN_PROGRESS. */
	public static final int IN_PROGRESS = 0;
	
	/** The Constant SUBMITTED. */
	public static final int SUBMITTED = 1;
	
	/** The Constant RETURNED. */
	public static final int RETURNED = 2;
	
	/** The Constant APPROVED. */
	public static final int APPROVED = 3;
	
	/** Creates a random id for the claim */
	private UUID uuid;
	
	
	/**
	 * Instantiates a new claim.
	 *
	 * @param claimName the claim name
	 */
	public Claim(String claimName) {
		this.uuid = UUID.randomUUID();
		this.claimName = claimName;
		this.expenseList = new ExpenseList();
		this.status = IN_PROGRESS;
		this.incomplete = true;
		this.tagIds = new ArrayList<UUID>();
		this.destinations = new ArrayList<Destination>();
	}
	
	/**
	 * Checks if is incomplete.
	 *
	 * @return true, if is incomplete
	 */
	public boolean isIncomplete() {
		return incomplete;
	}

	/**
	 * Sets the incomplete.
	 *
	 * @param context Needed for file IO
	 * @param incomplete the incompleteness indicator
	 */
	public void setIncomplete(Context context, boolean incomplete) {
		this.incomplete = incomplete;
		notifyViews(context);
	}
	/**
	 * Gets the Comments from the approver
	 * 
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the comments from the approver
	 * 
	 * @param comments
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}


	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}
	
	/**
	 * Sets the uuid.
	 *
	 * @param context Needed for file IO
	 * @param uuid the uuid
	 */
	public void setUuid(Context context, UUID uuid) {
		this.uuid = uuid;
		notifyViews(context);
	}	
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getSubmitterName(){
		return submitterName;
	}
	
	/**
	 * Set User name.
	 *
	 * @param context Needed for file IO
	 * @param submitterName the name
	 */
	public void setSubmitterName(Context context, String submitterName){
		this.submitterName = submitterName;
		notifyViews(context);
	}
	
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getSubmitterEmail(){
		return submitterEmail;
	}
	
	/**
	 * Set User name.
	 *
	 * @param context Needed for file IO
	 * @param submitterName the name
	 */
	public void setSubmitterEmail(Context context, String submitterEmail){
		this.submitterEmail = submitterEmail;
		notifyViews(context);
	}
	
	
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getApproverName(){
		return approverName;
	}
	
	/**
	 * Set User name.
	 *
	 * @param context Needed for file IO
	 * @param submitterName the name
	 */
	public void setApproverName(Context context, String approverName){
		this.approverName = approverName;
		notifyViews(context);
	}
	
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getApproverEmail(){
		return approverEmail;
	}
	
	/**
	 * Set User name.
	 *
	 * @param context Needed for file IO
	 * @param submitterName the name
	 */
	public void setApproverEmail(Context context, String approverEmail){
		this.approverEmail = approverEmail;
		notifyViews(context);
	}	
	
	/**
	 * Gets the claim name.
	 *
	 * @return the claim name
	 */
	public String getClaimName() {
		return claimName;
	}

	/**
	 * Sets the claim name.
	 *
	 * @param context Needed for file IO
	 * @param claimName the claim name
	 */
	public void setClaimName(Context context, String claimName) {
		this.claimName = claimName;
		notifyViews(context);
	}

	/**
	 * Gets the expense list.
	 *
	 * @return the expense list
	 */
	public ExpenseList getExpenseList() {
		return expenseList;
	}
	
	
	/**
	 * Sets the expense list.
	 *
	 * @param context Needed for file IO
	 * @param expenseList the expense list
	 */
	public void setExpenseList(Context context, ExpenseList expenseList) {
		this.expenseList = expenseList;
		notifyViews(context);
	}

	/**
	 * Sets the start date.
	 *
	 * @param context Needed for file IO
	 * @param d1 the date to use as the start date
	 */
	public void setStartDate(Context context, Calendar d1) {
		// TODO Auto-generated method stub
		this.startDate = d1;
		notifyViews(context);
	}
	
	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public Calendar getStartDate() {
		return startDate;
	}	
	
	/**
	 * Sets the end date.
	 *
	 * @param context Needed for file IO
	 * @param d2 the date to use as the end date
	 */
	public void setEndDate(Context context, Calendar d2){
		this.endDate = d2;
		notifyViews(context);
	}
	
	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public Calendar getEndDate() {
		return endDate;
	}
		
	/**
	 * Adds the destination.
	 *
	 * @param context Needed for file IO
	 * @param place the location of destination
	 * @param Reason the reason for travel to destination
	 */
	public void addDestination(Context context, String place, String reason){
		Destination travelInfo = new Destination();
		travelInfo.setDescription(reason);
		travelInfo.setName(place);
		
		destinations.add(travelInfo);
		notifyViews(context);
	}
	
	
	/**
	 * Sets the destination.
	 *
	 * @param context Needed for file IO
	 * @param destination the destination
	 */
	public void setDestinationList(Context context, ArrayList<Destination> destination) {
		this.destinations = destination;
		notifyViews(context);
	}
	
	/**
	 * Creates string of all destinations.
	 *
	 * @return the string
	 */
	public String toStringDestinations(){
		// Get the destinations in a list format
		String str_destinations = "";
		for ( int i = 0; i < destinations.size() - 1; i++ ){
			str_destinations += destinations.get(i).getName() + ", ";
		}
		if (destinations.size()>0)
			str_destinations += destinations.get(destinations.size() - 1).getName();
		return str_destinations;
	}
	
	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public ArrayList<Destination> getDestinationList() {
		return destinations;
	}	
	
	/**
	 * Sets the description.
	 *
	 * @param context Needed for file IO
	 * @param Description the description
	 */
	public void setDescription(Context context, String Description){
		this.description = Description;
		notifyViews(context);
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription(){
		return description; 
	}
	

	/**
	 * Sets the status.
	 *
	 * @param context Needed for file IO
	 * @param status the status
	 */
	public void setStatus(Context context, int status) {
		this.status = status;
		notifyViews(context);
	}
	
	public void setStatusNoNotify(Context context, int status) {
		this.status = status;
		Controller.getTagMap(context).saveData(context);
    	Controller.getClaimList(context).saveData(context);	
    	Controller.getUser(context).saveData(context);
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Updates claim status and sets comments. Does NOT update views.
	 * Needed becuase this must be called from within an update method.
	 * 
	 * @param context
	 * @param comments
	 */
	
	/**
	 * Updates claim status and sets comments. Does NOT update views.
	 * Needed becuase this must be called from within an update method.
	 * 
	 * @param context
	 * @param comments
	 */	
	public void markAsSubmitted(Context context){
		this.status = Claim.SUBMITTED;
		Controller.getClaimList(context).saveData(context);	
	}

	/** 
	 * compares the claim start date with the instance's start date
	 * @param arg0 The Claim to be compared
	 * @return the result of the comparison as an int
	 */
	@Override
	public int compareTo(Claim arg0) {
		if (startDate == null){
			return -1;
		}
		return (startDate.compareTo(arg0.getStartDate()))*-1;
	}

	/**
	 * Gets the list of TagIds.
	 * 
	 * @return the list of tag Ids
	 */
	public ArrayList<UUID> getTagsIds(Context context) {
		
		// First removes tags that no longer exist
		TagMap tm = Controller.getTagMap(context);
		ArrayList<UUID> toRemove = new ArrayList<UUID>();
		for ( UUID u : tagIds ){
			if ( ! tm.contains(u)){
				toRemove.add(u);
			}
		}
		for ( UUID u : toRemove ){
			tagIds.remove(u);
		}
				
		// Then returns the new list of tag ids
		return tagIds;
	}
	
	/**
	 * formats and gets a string list of the tags
	 * 
	 * @return a string of tags in list form
	 */
	public String toStringTags(Context context){
		String stringTags = "";
		TagMap tm = Controller.getTagMap(context);
		ArrayList<UUID> tagUuids = getTagsIds(context);
		
		for ( int i = 0; i < tagUuids.size() - 1; i++ ) {
			stringTags += tm.getTag(tagUuids.get(i)).toString() + ", "; 
		}
		
		if ( tagUuids.size() > 0 ){
			stringTags += tm.getTag(tagUuids.get(tagUuids.size() - 1)).toString();
		}
		
		return stringTags;
	}

	/**
	 * Sets the list of tagIds.
	 * 
	 * @param tagsIds the list of tagIds
	 */
	public void setTagsIds(ArrayList<UUID> tagsIds) {
		this.tagIds = tagsIds;
	}
	
	/**
	 * adds view to be updated
	 * @param view TView to be updated
	 */
	@Override
	public void addView(TView view){
		super.addView(view);
		expenseList.addView(view);
	}
	
}
