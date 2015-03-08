package group5.trackerexpress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;

public class ClaimController implements TController {

	private static final String FILENAME = "claims.sav";
	private Map<UUID, Claim> claims;
	private Context context;
	private static ClaimController instance;
	
	private ClaimController(Context context){
		this.context = context;
	}


	public static ClaimController getInstance() throws ExceptionControllerNotInitialized {
		if (instance == null)		
			throw new ExceptionControllerNotInitialized();
		instance.loadData();
		return instance;
	}
	
	/*!
	 * Initializes the claim controller making a singleton and storing a context
	 * for file IO. MAKE SURE ACTIVITY THAT INITIALIZES DOESN'T STOP, THATS WOULD BE BAD
	 */
	public static ClaimController initialize(Context context) throws ExceptionControllerAlreadyInitialized {
		if (instance != null)
			throw new ExceptionControllerAlreadyInitialized();
		instance = new ClaimController(context);
		return instance;
	}
	
	public String getClaim(UUID claimId){
		this.loadData();
		return claims.get(claimId).toString();
	}

	//Overwrites existing claim with same UUID as the claim passed in
	public void setClaim(Claim claim){
		this.loadData();
		claims.put(claim.getUuid(), claim);
		this.saveData();
	}
	
	//Adds a claim, taking either a claim object or just a string.
	//Returns the UUID, incase you need it
	public UUID addClaim(String claimString){
		this.loadData();
		Claim newClaim = new Claim(claimString);
		claims.put(newClaim.getUuid(), newClaim);
		this.saveData();
		return newClaim.getUuid();
	}
	
	public UUID addClaim(Claim newClaim){
		this.loadData();
		claims.put(newClaim.getUuid(), newClaim);
		this.saveData();
		return newClaim.getUuid();
	}
	
	public void deleteClaim(UUID claimId){
		this.loadData();
		claims.remove(claimId);
		this.saveData();
	}
	
	public void deleteClaim(Claim claim){
		this.loadData();
		claims.remove(claim);
		this.saveData();
	}


	public String getClaimName(UUID claimId){
		this.loadData();
		return claims.get(claimId).getClaimName();
	}

	public void setClaimName(UUID claimId, String name){
		this.loadData();
		claims.get(claimId).setClaimName(name);
		this.saveData();
	}
	
	public Date getStartDate(UUID claimId){
		this.loadData();
		return claims.get(claimId).getStartDate();
	}
	
	public void setStartDate(UUID claimId, Date date){
		this.loadData();
		claims.get(claimId).setStartDate(date);
		this.saveData();
	}
	
	public Date getEndDate(UUID claimId){
		this.loadData();
		return claims.get(claimId).getEndDate();
	}
	
	public void setEndDate(UUID claimId, Date date){
		this.loadData();
		claims.get(claimId).setEndDate(date);
		this.saveData();
	}
	
	private void saveData() {
		try {
			new FileCourrier<Map<UUID, Claim>>().saveFile(context, FILENAME, claims);
		} catch (IOException e) {
			System.err.println ("Could not save claims.");
			throw new RuntimeException();
		}
	}

	private void loadData() {
		try {
			this.claims = new FileCourrier<Map<UUID, Claim>>().loadFile(context, FILENAME);
		} catch (IOException e) {
			System.err.println ("Claims file not found, making a fresh claims list.");
			this.claims = new HashMap<UUID, Claim>();
		}
	}


	public int getNumClaims() {
		return claims.size();
	}

}
