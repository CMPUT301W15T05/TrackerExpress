package group5.trackerexpress.test;
import group5.trackerexpress.Claim;
import group5.trackerexpress.ClaimList;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import junit.framework.TestCase;


public class ClaimlistTest extends TestCase {
	
	
	
	public void testEmptyClaimList(){
		ClaimList claimList = new ClaimList();
		assertTrue("Empty claim list", claimList.size()==0);
	}
	
	
	
	public void testAddClaimList(){
		
		ClaimList claimList = new ClaimList();
		String claimName  = "A Claim";
		Claim testClaim = new Claim(claimName);
		claimList.add(testClaim);
		assertTrue("Empty claim list", claimList.size()==1);
		assertTrue("Test claim not contained", claimList.contains(testClaim));
	}
	
	
	
	public void testGetClaimList(){
		ClaimList claimList = new ClaimList();
		String claimName  = "A Claim";
		Claim testClaim = new Claim(claimName);
		
		claimList.add(testClaim);
		Collection<Claim> claim = claimList.getList();
		
		assertTrue("Empty claim list", claim.size()==1);
		assertTrue("Test claim not contained", claim.contains(testClaim));
	}
	
	
	
	public void testRemoveClaimList(){
		ClaimList claimList = new ClaimList();
		String claimName  = "A Claim";
		Claim testClaim = new Claim(claimName);
		claimList.add(testClaim);
		assertTrue("List size isn't big enough", claimList.size()==1);
		assertTrue("", claimList.contains(testClaim));
		claimList.remove(testClaim);
		assertTrue("List size isn't small enough", claimList.size()==0);
		assertFalse("test claim still contained", claimList.contains(testClaim));
	}
	
	
	public void sortByDates() {
        Comparator<ClaimList> comperator = new Comparator<ClaimList>() {
            @Override
            public int compareTo(ClaimDetails another) 
            {
                if (another == null) return 1;
                // sort descending, most recent first
                return another.date.compareTo(date);
            }

			@Override
			public int compare(ClaimList lhs, ClaimList rhs) {
				// TODO Auto-generated method stub
				return 0;
			}
        };
        Collections.sort(dataFromDB, comperator);
        lv1.setAdapter(new CustomListViewAdapter(this, dataFromDB));
    }
	
	
}
