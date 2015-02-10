import java.util.Collection;

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
	
	
}
