package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import android.content.Context;
import android.location.Location;


/**
 * Holds all of the apps claims. 
 * Provides functions for saving them, and loads them upon construction.  
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
	 * Instantiates a new claim list. 
	 * Loads data from save file.
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
	 * @param claim: the claim to be added
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
	 * @param context: Needed for file IO
	 * @param id: the id of the claim to be deleted
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
	 * gets an array of the claims sorted in reverse order
	 * 
	 * @return claim list as array sorted from oldest to newest.
	 */
	public Claim[] toListWithReverseSorting(){
		ArrayList<Claim> claimList = new ArrayList<Claim>(claims.values());
		
		// Comparator of claim switches rhs and lhs to achieve reverse sorting result
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
	 * Adds view to be updated
	 * 
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
	
	/**
	 * Returns a list of filtered Claims based on selected tags.
	 * 
	 * @param: context: required for getting list of claims from controller
	 * @return: list of filtered Claims depending on tags selected
	 */
	public static Claim[] getFilteredClaims(Context context){	
		// all the claims
		Claim[] listOfClaims = Controller.getClaimList(context).toList();		
		
		// will contain filtered claims
		ArrayList<Claim> filteredClaims = new ArrayList<Claim>();
	
		// all the tags
		ArrayList<Tag> listOfTags = Controller.getTagMap(context).toList();
		
		// tags to filtered
		ArrayList<Tag> filterTags = new ArrayList<Tag>();
		
		// gets the tags that need to be filtered
		for ( Tag t : listOfTags ){
			if ( t.isSelected() ){
				filterTags.add(t);
			}
		}
		
		// If all the tags are selected
		if ( filterTags.size() == listOfTags.size() ) {
			// Do not filter out anything even those without tags
			return listOfClaims;
		// Otherwise filter out claims based on if they have a filter tag
		} else {	
			for ( Claim c : listOfClaims ){
				ArrayList<UUID> tempTags = c.getTagsIds(context);
				for ( Tag t : filterTags ){
					if ( tempTags.contains(t.getUuid()) ){
						filteredClaims.add(c);
						break;
					}
				}
			}
		}
		
		// return the filtered claims
		return filteredClaims.toArray(new Claim[filteredClaims.size()]);
	}

	/** 
	 * Sorts the passed list of claims by how far they are from the user's location
	 * 
	 * @param context: context for file IO
	 * @param claimList: the list of claims to be sorted
	 * @return an array of claims sorted by their distance from the user's home
	 */
	public static Claim[] sortClaimsByLocation(Context context, Claim[] claimList){
		Claim[] ret = claimList.clone();
		
		// This is the user's home location
		final Location userLoc = Controller.getUser(context).getLocation();

		// This is for testing purposes for when the user DNE
		if ( userLoc == null ){
			return null;
		}
		
		// This sort uses a comparator that does a lot of null checks/
		// 		comparisons before actually comparing the geodistance
		// 		between two claims.
		Arrays.sort(claimList, new Comparator<Claim>() {
			@Override
			public int compare(Claim lhs, Claim rhs) {
				// null comparison
				if ( lhs.getDestinationList() == null ){
					return -1;
				} else if ( rhs.getDestinationList() == null ){
					return 1;
				}
				
				// gets the destination lists of each claim
				ArrayList<Destination> lDesList = lhs.getDestinationList();
				ArrayList<Destination> rDesList = rhs.getDestinationList();
				
				// checks if the the destination lists are empty and give a 
				//		comparison based on that information
				if ( lDesList.size() == 0 || lDesList.get(0).getLocation() == null ){
					return -1;
				} else if ( rDesList.size() == 0 || rDesList.get(0).getLocation() == null ){
					return 1;
				}
				
				// Gets the first location of the destination lists for each claim
				Location lLoc = lDesList.get(0).getLocation();
				Location rLoc = rDesList.get(0).getLocation();
				
				
				// compares the distance to home for each claim and returns 
				// 		the comparison result
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