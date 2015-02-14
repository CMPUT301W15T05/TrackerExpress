package group5.trackerexpress;

import java.util.GregorianCalendar;

import android.view.View;

public class Claim {
	
	protected String claimName;

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


	public void setDate(GregorianCalendar d1) {
		// TODO Auto-generated method stub
		
	}

}
