package group5.trackerexpress;

import android.view.View;

public class Claim {
	
	protected String claimName;

	public Claim(String claimName) {
		// TODO Auto-generated constructor stub
		this.claimName = claimName;
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

}
