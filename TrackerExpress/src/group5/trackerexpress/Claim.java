package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class Claim extends TModel implements Comparable<Claim>{
	
	private String userName;
	private String claimName;
	private String Description;
	private ExpenseList expenseList;
	private ArrayList<String[]> destination = new ArrayList<String[]>();
	private int status; 
	private Date startDate;
	private Date endDate;
	private boolean incomplete;
    
	public static final int IN_PROGRESS = 0;
	public static final int SUBMITTED = 1;
	public static final int RETURNED = 2;
	public static final int APPROVED = 3;
	
	private UUID uuid = UUID.randomUUID();
    
	
	public Claim(String claimName) {
		// TODO Auto-generated constructor stub
		this.claimName = claimName;
		this.expenseList = new ExpenseList();
		this.status = IN_PROGRESS;
	}
	
	public boolean isIncomplete() {
		return incomplete;
	}

	public void setIncomplete(Context context, boolean incomplete) {
		this.incomplete = incomplete;
		notifyViews(context);
	}

	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(Context context, UUID uuid) {
		this.uuid = uuid;
		notifyViews(context);
	}	
	
	public String getuserName(){
		return userName;
	}
	
	public void setuserName(Context context, String userName){
		this.userName = userName;
		notifyViews(context);
	}
	
	public String getClaimName() {
		return claimName;
	}

	public void setClaimName(Context context, String claimName) {
		this.claimName = claimName;
		notifyViews(context);
	}
	

	public void addExpense(Context context, Expense expense) {
		expenseList.addExpense(expense);
		notifyViews(context);		
	}


	public ExpenseList getExpenseList() {
		return expenseList;
	}
	
	
	public void setExpenseList(Context context, ExpenseList expenseList) {
		this.expenseList = expenseList;
		notifyViews(context);
	}
	

	public void removeExpense(Context context, UUID expenseUuid) {
		// TODO Auto-generated method stub
		expenseList.deleteExpense(expenseUuid);
		//expenseList.remove(expenseList.indexOf(string));
		notifyViews(context);		
	}


	public void setStartDate(Context context, Date d1) {
		// TODO Auto-generated method stub
		this.startDate = d1;
		notifyViews(context);
	}
	
	public Date getStartDate() {
		return startDate;
	}	
	
	public void setEndDate(Context context, Date d2){
		this.endDate = d2;
		notifyViews(context);
	}
	
	public Date getEndDate() {
		return endDate;
	}
		
	public void addDestination(Context context, String place, String Reason){
		String[] travelInfo = new String[2];
		travelInfo[0] = place;
		travelInfo[1] = Reason;
		destination.add(travelInfo);
		notifyViews(context);
	}
	
	
	public void setDestination(Context context, ArrayList<String[]> destination) {
		this.destination = destination;
		notifyViews(context);
	}
	
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
	
	public ArrayList<String[]> getDestination() {
		return destination;
	}	
	
	public void setDescription(Context context, String Description){
		this.Description = Description;
		notifyViews(context);
	}
	
	public String getDescription(){
		return Description; 
	}
	

	public void setStatus(Context context, int status) {
		this.status = status;
		notifyViews(context);
	}
	
	public int getStatus() {
		return status;
	}

	@Override
	public int compareTo(Claim arg0) {
		return startDate.compareTo(arg0.getStartDate());
	}

}
