package group5.trackerexpress.test;

import group5.trackerexpress.Claim;
import group5.trackerexpress.MainActivity;
import group5.trackerexpress.TestActivity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class ClaimTest extends ActivityInstrumentationTestCase2<TestActivity> {
	Context context;
	
	public ClaimTest() {
		super(TestActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		context = getActivity();
	}

	public void testToStringDestinations(){
		Claim testClaim = new Claim( "Claim1" );
		for ( int i = 0;  i < 6; i++ ){
			testClaim.addDestination(context, Integer.toString(i), Integer.toString(i+1));
		}
		String shouldLookLike = "0, 1, 2, 3, 4, 5";
		assertEquals( "Should be equal", shouldLookLike, testClaim.toStringDestinations() );
		
		testClaim = new Claim( "Claim1" );
		testClaim.addDestination(context, "1", "Reason");
		shouldLookLike = "1";
		assertEquals( "Should be equal", shouldLookLike, testClaim.toStringDestinations() );
	}
	
}

/*
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
*/
