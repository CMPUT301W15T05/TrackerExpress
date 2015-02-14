package group5.trackerexpress.test;
import group5.trackerexpress.Claim;
import group5.trackerexpress.ClaimList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;


public class ClaimListTest extends TestCase {
	
	
	
	public void testEmptyClaimList() {
		ClaimList claimList = new ClaimList();
		assertTrue("Empty claim list", claimList.size()==0);
	}
	
	
	
	public void testAddClaimList() {
		
		ClaimList claimList = new ClaimList();
		String claimName = "A Claim";
		Claim testClaim = new Claim(claimName);
		claimList.add(testClaim);
		assertTrue("Empty claim list", claimList.size()==1);
		assertTrue("Test claim not contained", claimList.contains(testClaim));
	}
	
	
	
	public void testGetClaimList() {
		ClaimList claimList = new ClaimList();
		String claimName = "A Claim";
		Claim testClaim = new Claim(claimName);
		
		claimList.add(testClaim);
		Collection<Claim> claim = claimList.getList();
		
		assertTrue("Empty claim list", claim.size()==1);
		assertTrue("Test claim not contained", claim.contains(testClaim));
	}
	
	
	
	public void testRemoveClaimList() {
		ClaimList claimList = new ClaimList();
		String claimName = "A Claim";
		Claim testClaim = new Claim(claimName);
		claimList.add(testClaim);
		assertTrue("List size isn't big enough", claimList.size()==1);
		assertTrue("Test claim not contained", claimList.contains(testClaim));
		claimList.remove(testClaim);
		assertTrue("List size isn't small enough", claimList.size()==0);
		assertFalse("test claim still contained", claimList.contains(testClaim));
	}
	
	
	public void testClaimOrder() {
		ClaimList claimList = new ClaimList();
		
		// Named so they come out in this order
		Claim c1 = new Claim("3");
		Claim c2 = new Claim("1");
		Claim c3 = new Claim("2");
		
		GregorianCalendar d1 = new GregorianCalendar();
		GregorianCalendar d2 = new GregorianCalendar();
		GregorianCalendar d3 = new GregorianCalendar();
		
		d1.set(Calendar.YEAR, 1995);
		d1.set(Calendar.MONTH, 11); // december = 11
		d1.set(Calendar.DAY_OF_MONTH, 12);
		
		d2.set(Calendar.YEAR, 1995);
		d2.set(Calendar.MONTH, 0); // january = 0
		d2.set(Calendar.DAY_OF_MONTH, 12);
		
		d3.set(Calendar.YEAR, 1995);
		d3.set(Calendar.MONTH, 1); // february = 1
		d3.set(Calendar.DAY_OF_MONTH, 12);
		
		
		c1.setStartDate(d1);
		c2.setStartDate(d2);
		c3.setStartDate(d3);
    	
		claimList.add(c1);
		claimList.add(c2);
		claimList.add(c3);
		
		assertTrue("List size isn't correct", claimList.size()==3);
		assertTrue("Claim '1' is not first", claimList.getList().get(0).getTitle().equals("1") );
		assertTrue("Claim '2' is not second", claimList.getList().get(1).getTitle().equals("2") );
		assertTrue("Claim '3' is not last", claimList.getList().get(2).getTitle().equals("3") );

	}
	
	
}
