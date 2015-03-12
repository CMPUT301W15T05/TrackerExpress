
package group5.trackerexpress.test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import group5.trackerexpress.Claim;
import group5.trackerexpress.ClaimList;
import group5.trackerexpress.Date;
import group5.trackerexpress.Expense;
import group5.trackerexpress.ExpenseNotFoundException;
import junit.framework.TestCase;

public class ClaimTest extends TestCase {

	private ClaimList claimList;
	
	public void setUp() {
		claimList = new ClaimList();
	}
	
	// this is test case for 01.01.01
	public void testAddClaim() {
		Claim claim = new Claim("Test Claim");
		claimList.add(claim);
		
		assertTrue("Claim not added to claimList", claimList.contains(claim));
	}
	
	// this is test case for 01.04.01
	public void testEditClaim() {
		Date d1 = new Date();

		d1.setYYYY(1995);
		d1.setMM(11);
		d1.setDD(12);
		
		Claim claim = new Claim("Test Claim");
		claimList.add(claim);
		
		UUID claimUUID = claim.getUuid();
		
		claim.setStartDate(d1);

		assertEquals("Claim not updated", claim.getStartDate(), d1);
		
		Claim testClaimEdited = claimList.getClaim(claimUUID);
		assertEquals("Claim not updated when grabbed from claim list", testClaimEdited.getStartDate(), d1);
	}
	
	// this is test case for 01.05.01
	public void testDeleteClaim() {
		Claim testClaim = new Claim("Test Claim");
		claimList.add(testClaim);
		
		UUID claimUUID = testClaim.getUuid();
		
		assertTrue("Claim not in claim list", claimList.size()==1);
		claimList.deleteClaim(claimUUID);
		
		assertTrue("Claim in claim list", claimList.size()==0);
	}
	
	public void testSubmitClaim() {
		Claim testClaim = new Claim("Test Claim");
		testClaim.setStatus(Claim.SUBMITTED);
		
		assertEquals("Claim was not submitted", testClaim.getStatus(), Claim.SUBMITTED);
	}
	
}

