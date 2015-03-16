package group5.trackerexpress;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class ClaimController.
 */
public class ClaimController implements TController {

	/** The claims. */
	private ClaimList claims;
	
	/** The instance. */
	private static ClaimController instance;
	
	/**
	 * Instantiates a new claim controller.
	 *
	 * @param context the context
	 */
	private ClaimController(Context context){
		this.claims = new ClaimList(context);
	}

	/**
	 * Gets the single instance of ClaimController.
	 *
	 * @param context the context
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
