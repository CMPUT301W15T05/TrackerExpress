package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import android.content.Context;


/**
 * Holds all of the apps claims. Provides functions for saving them, and loads them upon construction.  
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 * @see Claim
 */
public class ClaimList extends TModel{
	
	private static final long serialVersionUID = 1L;

	/** Map between a claim and its id. */
	private Map<UUID, Claim> claims;
	
	/** The Constant FILENAME for saving locally. */
	private static final String FILENAME = "claims.sav";
	
	/**
	 * Instantiates a new claim list. Loads data from save file.
	 *
	 * @param context Needed for file IO
	 */
	public ClaimList(Context context) {
		claims = new HashMap<UUID, Claim>();
		loadData(context);
	}

	/**
	 * Gets the claim.
	 *
	 * @param id the claim id
	 * @return the claim
	 */
	public Claim getClaim(UUID id){
		return claims.get(id);
	}

	/**
	 * Clears the map between claims and id
	 *
	 * @param context Needed for file IO
	 */
	public void clear(Context context){
		claims.clear();
		notifyViews(context);
	}

	/**
	 * Checks if the claim map is empty.
	 *
	 * @return true, if it is empty
	 */
	public boolean isEmpty() {
		return claims.isEmpty();
	}

	/**
	 * Adds the claim to the claims map.
	 *
	 * @param context Needed for file IO
	 * @param claim the claim to be added
	 */
	public void addClaim(Context context, Claim claim) {
		claims.put(claim.getUuid(), claim);
		makeSureViewsIsntNull();
		claim.addViews(this.views);
		notifyViews(context);
	}

	/**
	 * Delete claim from claims map.
	 *
	 * @param context Needed for file IO
	 * @param id the id of the claim to be deleted
	 */
	public void deleteClaim(Context context, UUID id) {
		claims.remove(id);
		notifyViews(context);
	}

	/**
	 * Gets size of claim map.
	 *
	 * @return the size of the claim map
	 */
	public int size() {
		return claims.size();
	}

	/**
	 * Gets all claims in an array of Claims.
	 *
	 * @return claimArray an array of Claims
	 */
	public Claim[] toList() {
		Claim[] claimArray = claims.values().toArray(new Claim[0]);
		Arrays.sort(claimArray);
		return claimArray;
	}
	
	
	/**
	 * adds view to be updated
	 * @param view TView to be updated
	 */
	@Override
	public void addView(TView view){
		super.addView(view);
		Iterator<Entry<UUID, Claim>> it = claims.entrySet().iterator();
		while (it.hasNext()) {
			it.next().getValue().addView(view);
		}
	}
	
	/**
	 * Save data in FILENAME using FileCourrier.
	 *
	 * @param context Needed for file IO
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
	 * Load data from FILENAME using FileCourrier into claims.
	 *
	 * @param context Needed for file IO
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