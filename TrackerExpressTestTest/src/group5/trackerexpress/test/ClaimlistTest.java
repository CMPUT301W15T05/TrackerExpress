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
		claimList.addClaim(testClaim);
		assertTrue("Empty claim list", claimList.size()==1);
		assertTrue("Test claim not contained", claimList.contains(testClaim));
	}
	
	
	
	public void testGetClaimList(){
		ClaimList claimList = new ClaimList();
		String claimName  = "A Claim";
		Claim testClaim = new Claim(claimName);
		
		claimList.addClaim(testClaim);
		Collection<Claim> claim = claimList.getClaim();
		
		assertTrue("Empty claim list", claim.size()==1);
		assertTrue("Test claim not contained", claim.contains(testClaim));
	}
	
	
	
	public void testRemoveClaimList(){
		ClaimList claimList = new ClaimList();
		String claimName  = "A Claim";
		Claim testClaim = new Claim(claimName);
		claimList.addClaim(testClaim);
		assertTrue("List size isn't big enough", claimList.size()==1);
		assertTrue("", claimList.contains(testClaim));
		claimList.removeClaim(testClaim);
		assertTrue("List size isn't small enough", claimList.size()==0);
		assertFalse("test claim still contained", claimList.contains(testClaim));
	}
	
	
	public void TestClaimOrder(){
		ClaimList claimList = new ClaimList();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		
		// will retrieve the text of the second column (date) of the first row (Claimlist).
		String pulled1 = claimList.ListItems.Item(1).ListSubItems.Item(1).Text;
		String pulled2 = claimList.ListItems.Item(2).ListSubItems.Item(1).Text;
		
		Date date1 = sdf.parse(pulled1);
    		Date date2 = sdf.parse(pulled2);
    	
    		assertTrue(date1.before(date2));
		
	}
	
	
}
