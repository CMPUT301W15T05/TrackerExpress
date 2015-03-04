package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

import android.view.View;

public class Claim {
	
	private String claimName;
	private ArrayList<Expense> expenseList;
	private ArrayList<String[]> destination = new ArrayList<String[]>();
	private int status; 
	private Date startDate;
	private Date endDate; 
    
	public static final int IN_PROGRESS = 0;
	public static final int SUBMITTED = 1;
	public static final int RETURNED = 2;
	public static final int APPROVED = 3;
	
	
	private UUID uuid = UUID.randomUUID();
    
	
	public Claim(String claimName) {
		// TODO Auto-generated constructor stub
		this.claimName = claimName;
		this.expenseList = new ArrayList<Expense>();
		
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}	
	
	
	
	public String getClaimName() {
		return claimName;
	}


	public void setClaimName(String claimName) {
		this.claimName = claimName;
	}
	

	public void addExpense(Expense expense) {
		expenseList.add(expense);
		
	}


	public ArrayList<Expense> getExpenseList() {
		// TODO Auto-generated method stub
		return expenseList;
	}
	
	
	public void setExpenseList(ArrayList<Expense> expenseList) {
		this.expenseList = expenseList;
	}
	
	
	

	public void removeExpense(String string) {
		// TODO Auto-generated method stub
		expenseList.remove(expenseList.indexOf(string));
	}


	public void setStartDate( Date d1) {
		// TODO Auto-generated method stub
		this.startDate = d1;
		
	}
	
	public Date getStartDate() {
		return startDate;
	}	
	
	public void setEndDate(Date d2){
		this.endDate = d2;
		
	}
	
	public Date getEndDate() {
		return endDate;
	}
		
	public void addDestination(String place, String descriptions){
		String[] travelInfo = new String[2];
		travelInfo[0] = place;
		travelInfo[1] = descriptions;
		destination.add(travelInfo);
	}
	
	public void setDestination(ArrayList<String[]> destination) {
		this.destination = destination;
	}
	
	public ArrayList<String[]> getDestination() {
		return destination;
	}	
	
	

	public void setStatus(int status) {
		// TODO Auto-generated method stub
		this.status = status;
		
	}
	
	public int getStatus() {
		return status;
		// TODO Auto-generated method stub
		
	}

}
