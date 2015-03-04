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
			claimIds.add( c.getUuid() );
		}
	}
	
	private void sort_claim_list() {
		// TODO Auto-generated method stub
		
		Collections.sort(claimList, new Comparator<Claim>(){

			@Override
			public int compare(Claim lhs, Claim rhs) {
				// TODO Auto-generated method stub	
				Integer lhs_date = lhs.getStartDate().getDate();
				Integer rhs_date = rhs.getStartDate().getDate();
				return lhs_date.compareTo(rhs_date);
			}
			
		});
	}

	public Claim getClaim(UUID claimId) {
		int index = claimIds.indexOf(claimId);
		return claimList.get(index);
	}

	public void deleteClaim(UUID claimId) {
		int index = claimIds.indexOf(claimId);
		try{
			claimIds.remove(index);
			claimList.remove(index);
		} catch( IndexOutOfBoundsException e ){
			throw new RuntimeException(e);
		}
	}

	public int size() {
		return claimList.size();
	}
}