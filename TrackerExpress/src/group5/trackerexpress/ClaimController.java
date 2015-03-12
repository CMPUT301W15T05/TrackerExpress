package group5.trackerexpress;

import android.content.Context;

public class ClaimController implements TController {

	private ClaimList claims;
	private static ClaimController instance;
	
	private ClaimController(Context context){
		this.claims = new ClaimList(context);
	}

	public static ClaimController getInstance(Context context){
		if (instance == null){		
			instance = new ClaimController(context);
		}
		return instance;
	}
	
	public ClaimList getClaimList(){
		return claims;
	}

}
