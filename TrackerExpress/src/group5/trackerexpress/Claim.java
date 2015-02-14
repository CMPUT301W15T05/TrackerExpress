package group5.trackerexpress;

import java.util.GregorianCalendar;

import android.view.View;

public class Claim {
	
	protected String claimName;

	public static final int IN_PROGRESS = 0;
	public static final int SUBMITTED = 1;
	public static final int RETURNED = 2;
	public static final int APPROVED = 3;

	public Claim(String claimName) {
		// TODO Auto-generated constructor stub
		this.claimName = claimName;
	}
	
	
	public Claim() {
		// TODO Auto-generated constructor stub
	}


	public String getName() {
		// TODO Auto-generated method stub
		return claimName;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return "";
	}


	public void addTag(View businessTag) {
		// TODO Auto-generated method stub
		
	}


	public void addExpense(Expense expense) {
		// TODO Auto-generated method stub
		
	}


	public Expense getExpense(String string) throws ExpenseNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}


	public void removeExpense(String string) {
		// TODO Auto-generated method stub
		
	}


	public void setStartDate(GregorianCalendar d1) {
		// TODO Auto-generated method stub
		
	}


	public GregorianCalendar getStartDate() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setStatus(int status) {
		// TODO Auto-generated method stub
		
	}
	
	public int getStatus() {
		return 0;
		// TODO Auto-generated method stub
		
	}

}
