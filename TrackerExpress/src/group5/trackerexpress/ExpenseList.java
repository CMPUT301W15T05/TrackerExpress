package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

/**
 * Holds the expenses for a single claim.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 * @see Claim, Expense
 */
public class ExpenseList extends TModel{
	
	
	private static final long serialVersionUID = 1L;

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
	public ArrayList<Expense> toList() {
		return expenseList;
	}

	/**
	 * Adds and expense to the expenseList.
	 *
	 * @param context Needed for file IO
	 * @param expense the expense
	 */
	public void addExpense(Context context, Expense expense) {
		expenseList.add(expense);
		expenseIds.add(expense.getUuid());
		expense.addViews(this.views);
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
	 * Removes the expense from the expenseList.
	 *
	 * @param context Needed for file IO
	 * @param expenseUuid the expense uuid
	 */
	public void removeExpense(Context context, UUID expenseId) {
		int index = expenseIds.indexOf(expenseId);
		try{
			expenseIds.remove(index);
			expenseList.remove(index);
		} catch ( IndexOutOfBoundsException e ){
			throw new RuntimeException(e);
		}
		notifyViews(context);
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
			int index = currencies.indexOf( e.getCurrency() );
			if ( index != -1 && e.getAmount() != null ){
				amounts.set(index, amounts.get(index) + e.getAmount() );
			} else {
				if ( e.getCurrency() != null && e.getAmount() != null ){
					currencies.add(e.getCurrency() );
					amounts.add(e.getAmount());
				}
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
	
	
	
	/**
	 * adds view to be updated
	 * @param view TView to be updated
	 */
	@Override
	public void addView(TView view){
		super.addView(view);
		for (Expense expense : expenseList)
			expense.addView(view);
	}

	/**
	 * @param expenseListFragment
	 */
	public void deleteView(TView view) {
		super.deleteView(view);
		for (Expense expense : expenseList){
			expense.deleteView(view);
		}
	}
	
}
