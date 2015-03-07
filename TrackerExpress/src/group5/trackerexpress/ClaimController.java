package group5.trackerexpress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;

public class ClaimController implements TController {

	private static final String FILENAME = "claims.sav";
	private Map<Long, Claim> claims;
	private static ClaimController claimController;
	
	private ClaimController(){

	}


	public static ClaimController getClaimController(Activity context) {
		if (claimController == null)		
			claimController = new ClaimController();
		claimController.loadData(context);
		return claimController;
	}
	
	public Claim getClaim(long claimId){
		return claims.get(claimId);
	}
	
	public long addClaimAndReturnId(Activity context, Claim claim){
		long claimId = new Random().nextLong();
		claims.put(claimId, claim);
		this.saveData(context);
		return claimId;
	}
	
	public Map<Long, Claim> getClaimMap(){
		return this.claims;
	}
	
	public void setClaimStartDate(Activity context, long claimId, Date newDate){
		claims.get(claimId).setStartDate(newDate);
		saveData(context);
	}
	
	private void saveData(Activity context) {
		try {
			new FileManager<Map<Long, Claim>>().saveFile(context, FILENAME, claims);
		} catch (IOException e) {
			System.err.println ("Could not save tags.");
			throw new RuntimeException();
		}
	}
	
	private void loadData(Activity context) {
		try {
			this.claims = new FileManager<Map<Long, Claim>>().loadFile(context, FILENAME);
		} catch (IOException e) {
			System.err.println ("Tags file not found, making a fresh tags list.");
			this.claims = new HashMap<Long, Claim>();
		}
	}

	
}
