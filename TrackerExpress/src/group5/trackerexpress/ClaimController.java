package group5.trackerexpress;

import android.content.Context;

/**
 * Singleton that manages the ClaimList
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class ClaimController implements TController {

	/** The claims. */
	private ClaimList claims;
	
	/** The instance. */
	private static ClaimController instance;
	
	/**
	 * Instantiates a new claim controller.
	 *
	 * @param context Needed for file IO
	 */
	private ClaimController(Context context){
		this.claims = new ClaimList(context);
	}

	/**
	 * Gets the single instance of ClaimController.
	 *
	 * @param context Needed for file IO
	 * @return single instance of ClaimController
	 */
	public static ClaimController getInstance(Context context){
		if (instance == null){		
			instance = new ClaimController(context);
		}
		return instance;
	}
	
	/**
	 * Gets the claim list.
	 *
	 * @return the claim list
	 */
	public ClaimList getClaimList(){
		return claims;
	}

}
