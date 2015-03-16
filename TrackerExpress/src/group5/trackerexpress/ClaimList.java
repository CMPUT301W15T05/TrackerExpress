package group5.trackerexpress;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;

public class ClaimList extends TModel{
	private Map<UUID, Claim> claims;
	private static final String FILENAME = "claims.sav";
	
	public ClaimList(Context context) {
		loadData(context);
	}

	public Claim getClaim(UUID id){
		return claims.get(id);
	}

	public void clear(Context context){
		claims.clear();
		notifyViews(context);
	}

	public boolean isEmpty() {
		return claims.isEmpty();
	}

	public void addClaim(Context context, Claim claim) {
		claims.put(claim.getUuid(), claim);
		claim.addViews(this.views);
		notifyViews(context);
	}

	public void deleteClaim(Context context, UUID id) {
		claims.remove(id);
		notifyViews(context);
	}

	public int size() {
		return claims.size();
	}

	public Claim[] getAllClaims() {
		Claim[] claimArray = claims.values().toArray(new Claim[0]);
		Arrays.sort(claimArray);
		return claimArray;
	}
	
	public void saveData(Context context) {
		try {
			new FileCourrier<ClaimList>(this).saveFile(context, FILENAME, this);
		} catch (IOException e) {
			System.err.println ("Could not save claims.");
			throw new RuntimeException();
		}
	}

	public void loadData(Context context) {
		ClaimList claimList;
		try {
			claimList = new FileCourrier<ClaimList>(this).loadFile(context, FILENAME);

			if (claimList == null || claimList.claims == null) {
				System.err.println ("ITS NULL");
				this.claims = new HashMap<UUID, Claim>();
			} else {
				this.claims = claimList.claims;
			}
		} catch (IOException e) {
			System.err.println ("Claims file not found, making a fresh claims list.");
			this.claims = new HashMap<UUID, Claim>();
		}
	}
}