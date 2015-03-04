package group5.trackerexpress;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public class ClaimList {
	private ArrayList<Claim> claimList;
	private ArrayList<UUID> claimIds;
	
	public ClaimList() {
		claimList = new ArrayList<Claim>();
		claimIds = new ArrayList<UUID>();
	}

	public ArrayList<Claim> getClaimList() {
		return claimList;
	}

	public void add(Claim claim) {
		claimList.add(claim);
		sort_claim_list();
		regenerateUUIDList();
	}
	
	private void regenerateUUIDList(){
		claimIds = new ArrayList<UUID>();
		
		for ( Claim c: claimList ){
			claimIds.add( c.getuuid() );
		}
	}
	
	private void sort_claim_list() {
		// TODO Auto-generated method stub
		
		Collections.sort(claimList, new Comparator<Claim>(){

			@Override
			public int compare(Claim lhs, Claim rhs) {
				// TODO Auto-generated method stub	
				int lhs_date = lhs.getStartDate().getDate();
				int rhs_date = rhs.getStartDate().getDate();
				return lhs_date.compareTo(rhs_date);
			}
			
		});
	}

	public Claim get(UUID claimId) {
		int index = claimIds.indexOf(claimId);
		return claimList.get(index);
	}

	public void remove(Claim claim) {
		claimList.remove(claim);
		regenerateUUIDList();
	}

	public int size() {
		return claimList.size();
	}

	public boolean contains(Claim claim) {
		return claimList.contains(claim);
	}
}