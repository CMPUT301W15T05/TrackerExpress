package group5.trackerexpress;

import java.util.UUID;

import android.content.Context;

/**
 * Holds all the static data members of the app.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Controller {

	/** The claims. */
	private static ClaimList claimList;
	private static TagMap tagMap;
	private static User user;
	
	/**
	 * Gets the static claim list, newing it if necessary
	 *
	 * @return the claim list
	 */
	public static ClaimList getClaimList(Context context){
		if (claimList == null)
			claimList = new ClaimList(context);
		return claimList;
	}
	
	/**
	 * Gets the static tag map, newing it if necessary
	 * 
	 * @param context
	 * @return
	 */

	public static TagMap getTagMap(Context context){
		if (tagMap == null)
			tagMap = new TagMap(context);
		return tagMap;
	}
	
	/**
	 * Gets the User data object. Right now there is but one user per phone
	 * 
	 * @param context
	 * @return
	 */
	public static User getUser(Context context){
		if (user == null)
			user = new User(context);
		return user;
	}
	
	public static Claim getClaim(Context context, UUID claimID){
		return getClaimList(context).getClaim(claimID);
	}
	
	public static Expense getExpense(Context context, UUID claimID, UUID expenseId){
		try {
			return getClaimList(context).getClaim(claimID).getExpenseList().getExpense(expenseId);
		} catch (ExpenseNotFoundException e) {
			throw new RuntimeException("Expense not found");
		}
	}
	


}
