package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class Claim.
 */
public class Claim extends TModel implements Comparable<Claim>{
	
	/** The user name. */
	private String userName;
	
	/** The claim name. */
	private String claimName;
	
	/** The Description. */
	private String Description;
	
	/** The expense list. */
	private ExpenseList expenseList;
	
	/** The destination. */
	private ArrayList<String[]> destination = new ArrayList<String[]>();
	
	/** The status. */
	private int status; 
	
	/** The start date. */
	private Date startDate;
	
	/** The end date. */
	private Date endDate;
	
	/** The incomplete. */
	private boolean incomplete;
	
	private ArrayList<UUID> tagsIds;
    
	/** The Constant IN_PROGRESS. */
	public static final int IN_PROGRESS = 0;
	
	/** The Constant SUBMITTED. */
	public static final int SUBMITTED = 1;
	
	/** The Constant RETURNED. */
	public static final int RETURNED = 2;
	
	/** The Constant APPROVED. */
	public static final int APPROVED = 3;
	
	/** The uuid. */
	private UUID uuid = UUID.randomUUID();
	
	
	/**
	 * Instantiates a new claim.
	 *
	 * @param claimName the claim name
	 */
	public Claim(String claimName) {
		// TODO Auto-generated constructor stub
		this.claimName = claimName;
		this.expenseList = new ExpenseList();
		this.status = IN_PROGRESS;
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
	 * @param context the context
	 * @param incomplete the incomplete
	 */
	public void setIncomplete(Context context, boolean incomplete) {
		this.incomplete = incomplete;
		notifyViews(context);
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
	 * @param context the context
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
	public String getuserName(){
		return userName;
	}
	
	/**
	 * Setuser name.
	 *
	 * @param context the context
	 * @param userName the user name
	 */
	public void setuserName(Context context, String userName){
		this.userName = userName;
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
	 * @param context the context
	 * @param claimName the claim name
	 */
	public void setClaimName(Context context, String claimName) {
		this.claimName = claimName;
		notifyViews(context);
	}
	

	/**
	 * Adds the expense.
	 *
	 * @param context the context
	 * @param expense the expense
	 */
	public void addExpense(Context context, Expense expense) {
		expenseList.addExpense(expense);
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
	 * @param context the context
	 * @param expenseList the expense list
	 */
	public void setExpenseList(Context context, ExpenseList expenseList) {
		this.expenseList = expenseList;
		notifyViews(context);
	}
	

	/**
	 * Removes the expense.
	 *
	 * @param context the context
	 * @param expenseUuid the expense uuid
	 */
	public void removeExpense(Context context, UUID expenseUuid) {
		// TODO Auto-generated method stub
		expenseList.deleteExpense(expenseUuid);
		//expenseList.remove(expenseList.indexOf(string));
		notifyViews(context);		
	}


	/**
	 * Sets the start date.
	 *
	 * @param context the context
	 * @param d1 the d1
	 */
	public void setStartDate(Context context, Date d1) {
		// TODO Auto-generated method stub
		this.startDate = d1;
		notifyViews(context);
	}
	
	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}	
	
	/**
	 * Sets the end date.
	 *
	 * @param context the context
	 * @param d2 the d2
	 */
	public void setEndDate(Context context, Date d2){
		this.endDate = d2;
		notifyViews(context);
	}
	
	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public Date getEndDate() {
		return endDate;
	}
		
	/**
	 * Adds the destination.
	 *
	 * @param context the context
	 * @param place the place
	 * @param Reason the reason
	 */
	public void addDestination(Context context, String place, String Reason){
		String[] travelInfo = new String[2];
		travelInfo[0] = place;
		travelInfo[1] = Reason;
		destination.add(travelInfo);
		notifyViews(context);
	}
	
	
	/**
	 * Sets the destination.
	 *
	 * @param context the context
	 * @param destination the destination
	 */
	public void setDestination(Context context, ArrayList<String[]> destination) {
		this.destination = destination;
		notifyViews(context);
	}
	
	/**
	 * To string destinations.
	 *
	 * @return the string
	 */
	public String toStringDestinations(){
		// Get the destinations in a list format
		String str_destinations = "";
		for ( int i = 0; i < destination.size() - 1; i++ ){
			str_destinations += destination.get(i)[0] + ", ";
		}
		if (destination.size()>0)
			str_destinations += destination.get(destination.size() - 1)[0];
		return str_destinations;
	}
	
	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public ArrayList<String[]> getDestination() {
		return destination;
	}	
	
	/**
	 * Sets the description.
	 *
	 * @param context the context
	 * @param Description the description
	 */
	public void setDescription(Context context, String Description){
		this.Description = Description;
		notifyViews(context);
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription(){
		return Description; 
	}
	

	/**
	 * Sets the status.
	 *
	 * @param context the context
	 * @param status the status
	 */
	public void setStatus(Context context, int status) {
		this.status = status;
		notifyViews(context);
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Claim arg0) {
		if (startDate == null){
			return -1;
		}
		return startDate.compareTo(arg0.getStartDate());
	}

	public ArrayList<UUID> getTagsIds() {
		return tagsIds;
	}

	public void setTagsIds(ArrayList<UUID> tagsIds) {
		this.tagsIds = tagsIds;
	}
	
}
