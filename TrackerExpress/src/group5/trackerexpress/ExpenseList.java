package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class ExpenseList.
 */
public class ExpenseList {
	
	/** The expense list. */
	private ArrayList<Expense> expenseList;
	
	/** The expense ids. */
	private ArrayList<UUID> expenseIds;
	
	/**
	 * Instantiates a new expense list.
	 */
	public ExpenseList() {
		expenseList = new ArrayList<Expense>();
		expenseIds = new ArrayList<UUID>();
	}
	
	/**
	 * Gets the expense list.
	 *
	 * @return the expense list
	 */
	public ArrayList<Expense> getExpenseList() {
		return expenseList;
	}

	/**
	 * Adds the expense.
	 *
	 * @param expense the expense
	 */
	public void addExpense(Expense expense) {
		expenseList.add(expense);
		expenseIds.add(expense.getUuid());
	}
	
	/**
	 * Gets the expense.
	 *
	 * @param expenseId the expense id
	 * @return the expense
	 * @throws ExpenseNotFoundException the expense not found exception
	 */
	public Expense getExpense(UUID expenseId) throws ExpenseNotFoundException {
		int index = expenseIds.indexOf(expenseId);
		
		return expenseList.get(index);
	}

	/**
	 * Delete expense.
	 *
	 * @param expenseId the expense id
	 */
	public void deleteExpense(UUID expenseId) {
		int index = expenseIds.indexOf(expenseId);
		try{
			expenseIds.remove(index);
			expenseList.remove(index);
		} catch ( IndexOutOfBoundsException e ){
			throw new RuntimeException(e);
		}
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return expenseList.size();
	}
	
	/**
	 * To string total currencies.
	 *
	 * @return the string
	 */
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
