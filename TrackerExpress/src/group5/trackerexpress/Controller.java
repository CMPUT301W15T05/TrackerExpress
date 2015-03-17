package group5.trackerexpress;

import android.content.Context;

/**
 * Singleton that manages the ClaimList
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
	 * Gets the claim list.
	 *
	 * @return the claim list
	 */
	public static ClaimList getClaimList(Context context){
		if (claimList == null)
			claimList = new ClaimList(context);
		return claimList;
	}
	
	public static TagMap getTagMap(Context context){
		if (tagMap == null)
			tagMap = new TagMap(context);
		return tagMap;
	}
	
	public static User getUser(Context context){
		if (user == null)
			user = new User(context);
		return user;
	}
	


}
