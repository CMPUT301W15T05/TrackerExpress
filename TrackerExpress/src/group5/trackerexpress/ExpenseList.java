package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

public class ExpenseList {
	private ArrayList<Expense> expenseList;
	private ArrayList<UUID> expenseIds;
	
	public ExpenseList() {
		expenseList = new ArrayList<Expense>();
		expenseIds = new ArrayList<UUID>();
	}
	
	public ArrayList<Expense> getExpenseList() {
		return expenseList;
	}

	public void addExpense(Expense expense) {
		expenseList.add(expense);
		expenseIds.add(expense.getUuid());
	}
	
	public Expense getExpense(UUID expenseId) throws ExpenseNotFoundException {
		int index = expenseIds.indexOf(expenseId);
		
		return expenseList.get(index);
	}

	public void deleteExpense(UUID expenseId) {
		int index = expenseIds.indexOf(expenseId);
		try{
			expenseIds.remove(index);
			expenseList.remove(index);
		} catch ( IndexOutOfBoundsException e ){
			throw new RuntimeException(e);
		}
	}

	public int size() {
		return expenseList.size();
	}
}
