package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class ClaimList.
 */
public class ClaimList extends TModel{
	
	/** The claims. */
	private Map<UUID, Claim> claims;
	
	/** The Constant FILENAME. */
	private static final String FILENAME = "claims.sav";
	
	/**
	 * Instantiates a new claim list.
	 *
	 * @param context the context
	 */
	public ClaimList(Context context) {
		claims = new HashMap<UUID, Claim>();
		loadData(context);
	}

	/**
	 * Gets the claim.
	 *
	 * @param id the id
	 * @return the claim
	 */
	public Claim getClaim(UUID id){
		return claims.get(id);
	}

	/**
	 * Clear.
	 *
	 * @param context the context
	 */
	public void clear(Context context){
		claims.clear();
		notifyViews(context);
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return claims.isEmpty();
	}

	/**
	 * Adds the claim.
	 *
	 * @param context the context
	 * @param claim the claim
	 */
	public void addClaim(Context context, Claim claim) {
		claims.put(claim.getUuid(), claim);
		makeSureViewsIsntNull();
		claim.addViews(this.views);
		notifyViews(context);
	}

	/**
	 * Delete claim.
	 *
	 * @param context the context
	 * @param id the id
	 */
	public void deleteClaim(Context context, UUID id) {
		claims.remove(id);
		notifyViews(context);
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return claims.size();
	}

	/**
	 * Gets the all claims.
	 *
	 * @return the all claims
	 */
	public Claim[] getAllClaims() {
		Claim[] claimArray = claims.values().toArray(new Claim[0]);
		Arrays.sort(claimArray);
		return claimArray;
	}
	
	/**
	 * Save data.
	 *
	 * @param context the context
	 */
	public void saveData(Context context) {
		try {
			new FileCourrier<ClaimList>(this).saveFile(context, FILENAME, this);
		} catch (IOException e) {
			System.err.println ("Could not save claims.");
			throw new RuntimeException();
		}
	}

	/**
	 * Load data.
	 *
	 * @param context the context
	 */
	public void loadData(Context context) {
		try {
			this.claims = new FileCourrier<ClaimList>(this).loadFile(context, FILENAME).claims;
		} catch (FileNotFoundException e) { 
			System.err.println ("Claims file not found, making a fresh claims list.");
			this.claims = new HashMap<UUID, Claim>();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
}