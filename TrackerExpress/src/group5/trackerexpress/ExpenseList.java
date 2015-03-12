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
	
	public String toStringTotalCurrencies(){
		String ret = "";
		ArrayList<String> currencies = new ArrayList<String>();
		ArrayList<Double> amounts = new ArrayList<Double>();
		
		for ( Expense e: expenseList ){
			int index = amounts.indexOf( e.getCurrency() );
			if ( index != -1 ){
				amounts.set(index, amounts.get(index) + e.getAmount() );
			} else {
				currencies.add(e.getCurrency() );
				amounts.add(0.0);
			}
		}
		
		for ( int i = 0; i < currencies.size() - 1; i++ ){
			ret += amounts.get(i) + " " + currencies.get(i) + ", ";
		}
		if ( currencies.size() > 1 ){
			ret += amounts.get(amounts.size()-1) + " " + currencies.get(currencies.size()-1);
		}
		
		return ret;
	}
}
