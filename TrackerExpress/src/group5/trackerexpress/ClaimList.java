package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import android.content.Context;
import android.location.Location;


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
	 * Gets all claims in an array of Claims, sorted newest to oldest.
	 *
	 * @return claimArray an array of Claims
	 */
	public Claim[] toList() {
		Claim[] claimArray = claims.values().toArray(new Claim[0]);
		Arrays.sort(claimArray);
		return claimArray;
	}
	
	/**
	 * 
	 * @return claim as array sorted from oldest to newest.
	 */
	public Claim[] toListWithReverseSorting(){
		ArrayList<Claim> claimList = new ArrayList<Claim>(claims.values());
		
		Comparator<Claim> comparator = new Comparator<Claim>(){
			@Override
			public int compare(Claim lhs, Claim rhs) {
				return lhs.getStartDate().compareTo(rhs.getStartDate());
			}
		};
		
		Collections.sort(claimList, comparator);
		Claim[] claimArray = claimList.toArray(new Claim[0]);
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
	
	/** Sorts the passed list of claims by how far they are from the user's location */
	public static Claim[] sortClaimsByLocation(Context context, Claim[] claimList){
		Claim[] ret = claimList.clone();
		
		final Location userLoc = Controller.getUser(context).getLocation();

		// This is for testing purposes
		if ( userLoc == null ){
			return ret;
		}
		
		Arrays.sort(claimList, new Comparator<Claim>() {

			@Override
			public int compare(Claim lhs, Claim rhs) {
				// TODO Auto-generated method stub
				if ( lhs.getDestinationList() == null ){
					return -1;
				} else if ( rhs.getDestinationList() == null ){
					return 1;
				}
				
				ArrayList<Destination> lDesList = lhs.getDestinationList();
				ArrayList<Destination> rDesList = rhs.getDestinationList();
				
				if ( lDesList.size() == 0 || lDesList.get(0).getLocation() == null ){
					return -1;
				} else if ( rDesList.size() == 0 || rDesList.get(0).getLocation() == null ){
					return 1;
				}
				
				Location lLoc = lDesList.get(0).getLocation();
				Location rLoc = rDesList.get(0).getLocation();
				
				if ( userLoc.distanceTo(lLoc) < userLoc.distanceTo(rLoc) ){
					return -1;
				} else {
					return 1;
				}
			}
			
		});
		
		return ret;
	}
}