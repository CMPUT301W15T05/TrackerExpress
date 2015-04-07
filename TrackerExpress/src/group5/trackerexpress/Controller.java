package group5.trackerexpress;


import java.io.IOException;
import java.util.UUID;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Holds all the static data members of the app.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Controller {

	/** The claims saved locally */
	private static ClaimList claimList;
	
	/** the tag map for tag information */
	private static TagMap tagMap;
	
	/** the user of the application */
	private static User user;
	
	/** The context of the Base Activity*/
	private static Context context;
	/**
	 * Gets the static claim list, newing it if necessary
	 *
	 * @param context: if newing is necessary
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
	 * @param context: to instantiate the tagmap if necessary
	 * @return the tagMap
	 */

	public static TagMap getTagMap(Context context){
		if (tagMap == null)
			tagMap = new TagMap(context);
		return tagMap;
	}
	
	/**
	 * Gets the User data object. Right now there is but one user per phone
	 * 
	 * @param context: to instantiate the user if necessary
	 * @return the User
	 */
	public static User getUser(Context context){
		if (user == null)
			user = new User(context);
		return user;
	}
	

	public static void setUser(Context context, User user2) {
		user = user2;
		user2.notifyViews(context);
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
	 * @return true if internet is connected
	 * 			false if internet is disconnected
	 */
	public static boolean isInternetConnected(Context contextIn){
		ConnectivityManager cm;
		try {
			cm = (ConnectivityManager) contextIn.getSystemService(Context.CONNECTIVITY_SERVICE);
		} catch (Exception e) {
			cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		               		activeNetwork.isConnectedOrConnecting();
		
		return isConnected;
	}
	
	public static void updateClaimsFromInternet(Context context){
		// updates the claims from the internet if it is connected
		if ( isInternetConnected(context) ){
			Claim[] localListOfClaims = Controller.getClaimList(context).toList();
			Claim[] elasticListOfClaims;
			try {
				elasticListOfClaims = (new ElasticSearchEngineClaims()).getClaims(context);
			} catch (IOException e) {
				//FIXME: Notify user of elastic search fail
				e.printStackTrace();
				elasticListOfClaims = new Claim[0];
			}
			for ( Claim c : localListOfClaims ){
				Log.i("TESTING", c.getUuid().toString() + c.getClaimName());
				if ( c.getStatus() == Claim.SUBMITTED){
					int index = 0;
					boolean found = false;
					for (Claim g : elasticListOfClaims){
						Log.i("GLOBALNAMES", g.getUuid() + " " + g.getClaimName());
						if (g.getUuid().toString().equals(c.getUuid().toString())) {
							Log.i("CLAIMNAME", g.getClaimName());
							found = true;
							break;
						}
						index++;	
					}
					if (!found)
						continue;
					if ( index != -1 && elasticListOfClaims[index].getStatus() != Claim.SUBMITTED) {
						claimList.getClaim(c.getUuid()).setStatus(context, elasticListOfClaims[index].getStatus());
						claimList.getClaim(c.getUuid()).setComments(elasticListOfClaims[index].getComments());
						claimList.getClaim(c.getUuid()).setApproverEmail(context, elasticListOfClaims[index].getApproverEmail());
						claimList.getClaim(c.getUuid()).setApproverName(context, elasticListOfClaims[index].getApproverName());
					}
				}
			}
		}
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		Controller.context = context;
	}

}
