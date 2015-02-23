package group5.trackerexpress.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import group5.trackerexpress.Claim;
import group5.trackerexpress.ClaimList;
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
		GregorianCalendar d1 = new GregorianCalendar();

		d1.set(Calendar.YEAR, 1995);
		d1.set(Calendar.MONTH, 11);
		d1.set(Calendar.DAY_OF_MONTH, 12);
		
		Claim claim = new Claim("Test Claim");
		claimList.add(claim);
		
		claim.setStartDate(d1);

		assertEquals("Claim not updated", claim.getStartDate(), d1);
		
		Claim testClaimEdited = claimList.get("Test Claim");
		assertEquals("Claim not updated when grabbed from claim list", testClaimEdited.getStartDate(), d1);
	}
	
	// this is test case for 01.05.01
	public void testDeleteClaim() {
		Claim testClaim = new Claim("Test Claim");
		claimList.add(testClaim);
		
		assertTrue("Claim not in claim list", claimList.size()==1);
		claimList.remove(testClaim);
		
		assertTrue("Claim in claim list", claimList.size()==0);
	}
	
	public void testSubmitClaim() {
		Claim testClaim = new Claim("Test Claim");
		testClaim.setStatus(Claim.SUBMITTED);
		
		assertEquals("Claim was not submitted", testClaim.getStatus(), Claim.SUBMITTED);
	}
	
}
