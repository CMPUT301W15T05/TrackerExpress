package group5.trackerexpress;

import java.util.ArrayList;

public class ExpenseList {
	private ArrayList<Expense> expenseList;
	private ArrayList<Integer> expenseIds;
	
	public ExpenseList() {
		expenseList = new ArrayList<Expense>();
	}
	
	public ArrayList<Expense> getList() {
		return expenseList;
	}

	public void add(Expense expense) {
		expenseList.add(expense);
	}
	
	public Expense get(String expenseId) {
		return null;
	}

	public void remove(Expense testExpense) {
		expenseList.remove(testExpense);
		
	}

	public int size() {
		return expenseList.size();
	}

	public boolean contains(Expense testExpense) {
		return expenseList.contains(testExpense);
	}

}
