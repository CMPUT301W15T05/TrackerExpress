package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

import android.view.View;

public class Claim {
	
	private String claimName;
	private ArrayList<Expense> expenseList;
	private ArrayList<String> destination = new ArrayList<String>();
	private int status; 
	private Date startDate;
	private Date endDate; 
    
	public static final int IN_PROGRESS = 0;
	public static final int SUBMITTED = 1;
	public static final int RETURNED = 2;
	public static final int APPROVED = 3;
	
	
	UUID uuid = UUID.randomUUID();
    
	public Claim(String claimName) {
		// TODO Auto-generated constructor stub
		this.claimName = claimName;
		this.expenseList = new ArrayList<Expense>();
		
	}
	

	public String getName() {
		// TODO Auto-generated method stub
		return this.claimName;
	}


	public void addExpense(Expense expense) {
		expenseList.add(expense);
		
	}


	public ArrayList<Expense> getExpense(String string) throws ExpenseNotFoundException {
		// TODO Auto-generated method stub
		return expenseList;
	}


	public void removeExpense(String string) {
		// TODO Auto-generated method stub
		expenseList.remove(expenseList.indexOf(string));
	}


	public void setStartDate( Date d1) {
		// TODO Auto-generated method stub
		this.startDate = d1;
		
	}
	
	public void setEndDate(Date d2){
		this.endDate = d2;
		
	}
	
	public void addDestination(String string){
		destination.add(string);
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
