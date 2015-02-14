package group5.trackerexpress;

import java.util.ArrayList;
import java.util.Collection;


public class ClaimList {
	protected ArrayList<Claim> claimList;
	
	public ClaimList(){
		claimList = new ArrayList<Claim>();
	}

	public ArrayList<Claim> getList() {
		return claimList;
	}

	public void add(Claim testClaim) {
		claimList.add(testClaim);
		 
	}
	
	public Claim get(String claimId) {
		return null;
	}

	public void remove(Claim testClaim) {
		claimList.remove(testClaim);
		
	}

	public int size() {
		return claimList.size();
	}

	public boolean contains(Claim testClaim) {
		return claimList.contains(testClaim);
	}
}
	
	
