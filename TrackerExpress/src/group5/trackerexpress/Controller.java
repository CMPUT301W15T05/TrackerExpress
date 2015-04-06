package group5.trackerexpress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
	
	/**
	 * Gets the claim based on the claim id
	 * 
	 * @param context
	 * @param claimID: id of the claim
	 * @return the desired claim
	 */
	public static Claim getClaim(Context context, UUID claimID){
		return getClaimList(context).getClaim(claimID);
	}
	
	/**
	 * Gets the expense base on the claim id and the expense id
	 * 
	 * @param context
	 * @param claimID: id of claim of expense
	 * @param expenseId: id of expense
	 * @return: the desired expense
	 */
	public static Expense getExpense(Context context, UUID claimID, UUID expenseId){
		try {
			return getClaimList(context).getClaim(claimID).getExpenseList().getExpense(expenseId);
		} catch (ExpenseNotFoundException e) {
			throw new RuntimeException("Expense not found");
		}
	}
	
	/** 
	 * Checks if internet is connected.
	 * 
	 * @param: context
	 * @return boolean based on if internet is connected
	 */
	public static boolean isInternetConnected(Context context){
		ConnectivityManager cm =
		        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		               		activeNetwork.isConnectedOrConnecting();
		
		return isConnected;
	}
	
	/**
	 * Returns a list of filtered Claims for the MyClaims Fragment.
	 * 
	 * @param: context
	 * @return: list of filtered Claims depending on tags selected
	 */
	public static Claim[] getFilteredClaims(Context context){		
		Claim[] listOfClaims = Controller.getClaimList(context).toList();		
		ArrayList<Claim> filteredClaims = new ArrayList<Claim>();

		ArrayList<Tag> listOfTags = Controller.getTagMap(context).toList();
		ArrayList<Tag> filterTags = new ArrayList<Tag>();
		
		for ( Tag t : listOfTags ){
			if ( t.isSelected() ){
				filterTags.add(t);
			}
		}
		
		// If all the tags are selected
		if ( filterTags.size() == listOfTags.size() ) {
			// Do not filter out anything
			// Even those without tags
			return listOfClaims;
		} else {	
			for ( Claim c : listOfClaims ){
				ArrayList<UUID> tempTags = c.getTagsIds(context);
				for ( Tag t : filterTags ){
					if ( tempTags.contains(t.getUuid()) ){
						filteredClaims.add(c);
						break;
					}
				}
			}
		}
		
		return filteredClaims.toArray(new Claim[filteredClaims.size()]);
	}

	private static void updateClaimsFromInternet(Context context){
		if ( isInternetConnected(context) ){
			Claim[] localListOfClaims = Controller.getClaimList(context).toList();
			Claim[] elasticListOfClaims = (new ElasticSearchEngine()).getClaims();
			
			for ( Claim c : localListOfClaims ){
				if ( c.getStatus() == Claim.SUBMITTED ){
					int index = Arrays.asList(elasticListOfClaims).indexOf(c);
					if ( index != -1 && elasticListOfClaims[index].getStatus() != Claim.SUBMITTED) {
						c.setStatus(context, elasticListOfClaims[index].getStatus());
					}
				}
			}
		}
	}
}
