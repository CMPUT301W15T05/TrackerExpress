package group5.trackerexpress.test;

import java.util.UUID;

import group5.trackerexpress.Claim;
import group5.trackerexpress.ClaimList;
import group5.trackerexpress.Date;
import group5.trackerexpress.MainActivity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class ClaimListTest extends ActivityInstrumentationTestCase2<MainActivity> {
	Context context;
	
	public ClaimListTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		context = getActivity();
	}

	public void testEmptyClaimList() {
		ClaimList claimList = new ClaimList(context);
		assertTrue("Empty claim list", claimList.size()==0);
	}
	
	
	// this is test case for 01.01.01 and 01.02.01
	public void testAddClaimList() {
		ClaimList claimList = new ClaimList(context);
		String claimName = "A Claim";
		Claim testClaim = new Claim(claimName);
		claimList.addClaim(context, testClaim);
		assertTrue("Empty claim list", claimList.size()==1);
		assertTrue("Test claim not contained", 
					testClaim.equals(claimList.getClaim(testClaim.getUuid())));
	}
	
	
	// this is test case for 01.03.01
	public void testGetClaimList() {
		ClaimList claimList = new ClaimList(context);
		String claimName = "A Claim";
		Claim testClaim = new Claim(claimName);
		
		claimList.addClaim(context, testClaim);
		
		assertTrue("Empty claim list", claimList.size()==1);
		assertTrue("Test claim not contained",
					testClaim.equals( claimList.getClaim(testClaim.getUuid()) ));
	}
	
	
	// this is test case for 01.05.01
	public void testRemoveClaimList() {
		ClaimList claimList = new ClaimList(context);
		Claim testClaim = new Claim("Test Claim");
		UUID testUuid = testClaim.getUuid();
		claimList.addClaim(context, testClaim);
		assertTrue("List size isn't big enough", claimList.size()==1);
		assertTrue("Test claim not contained", 
					testClaim.equals(claimList.getClaim(testClaim.getUuid())));
		claimList.deleteClaim(context, testClaim.getUuid());
		assertTrue("List size isn't small enough", claimList.size()==0);
		Claim retrieved = claimList.getClaim(testUuid);
		assertTrue ( "nothing should be there", retrieved == null );
	}
	
	// this is test case for 02.02.01 and 02.01.01
	public void testClaimOrder() {
		ClaimList claimList = new ClaimList(context);
		
		// Named so they come out in this order
		Claim c1 = new Claim("3");
		Claim c2 = new Claim("1");
		Claim c3 = new Claim("2");
		
		
		Date d1 = new Date(1995, 12, 12);
		Date d2 = new Date(1995, 1, 12);
		Date d3 = new Date(1995, 2, 12);
		
		c1.setStartDate(context, d1);
		c2.setStartDate(context, d2);
		c3.setStartDate(context, d3);
    	
		claimList.addClaim(context, c1);
		claimList.addClaim(context, c2);
		claimList.addClaim(context, c3);
		
		assertTrue("List size isn't correct", claimList.size()==3);
		assertTrue("Claim '1' is not first", claimList.getAllClaims()[0].getClaimName().equals("1") );
		assertTrue("Claim '2' is not second", claimList.getAllClaims()[1].getClaimName().equals("2") );
		assertTrue("Claim '3' is not last", claimList.getAllClaims()[2].getClaimName().equals("3") );
		
	}
}
